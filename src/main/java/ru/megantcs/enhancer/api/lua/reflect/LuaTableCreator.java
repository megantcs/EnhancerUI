package ru.megantcs.enhancer.api.lua.reflect;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import ru.megantcs.enhancer.api.lua.LuaConvertor;
import ru.megantcs.enhancer.api.lua.LuaScriptEngine;
import ru.megantcs.enhancer.api.lua.reflect.wrappers.FieldWrapper;
import ru.megantcs.enhancer.api.lua.reflect.wrappers.MethodWrapper;
import ru.megantcs.enhancer.platform.toolkit.exceptions.*;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionContainer;
import ru.megantcs.enhancer.platform.toolkit.reflect.AnnotationSearcher;

import java.lang.reflect.*;
import java.util.Objects;

public class LuaTableCreator
{
    public static LuaTable exportClassTable(Object instance, ExceptionContainer exceptionContainer) {
        Objects.requireNonNull(instance);
        Objects.requireNonNull(exceptionContainer);

        var clazz = instance.getClass();
        var table = new LuaTable();

        LuaExportClass annotation = clazz.getAnnotation(LuaExportClass.class);
        if(annotation == null) {
            exceptionContainer.add("LuaTableCreator", new MissingAnnotationException(LuaExportClass.class));
            return null;
        }

        var fields = AnnotationSearcher.getFields(clazz, LuaExportField.class);
        var methods = AnnotationSearcher.getMethods(clazz, LuaExportMethod.class);

        for (Field field : fields) {
            if(!exportField(table, field, instance)) {
                exceptionContainer.add("LuaTableCreator", new MissingFieldException(field.getName() + "@" + field.toGenericString()));
            }
        }
        for (Method method : methods) {
            if(!exportMethod(table, method, instance)) {
                exceptionContainer.add("LuaTableCreator", new MissingMethodException(method.getName() + "@" + method.toGenericString()));
            }
        }

        return table;
    }

    public static boolean exportClass(Object instance, LuaScriptEngine sandbox, ExceptionContainer exceptionContainer) {

        var clazz = instance.getClass();
        LuaExportClass annotation = clazz.getAnnotation(LuaExportClass.class);
        if(annotation == null) {
            exceptionContainer.add("LuaTableCreator", new MissingAnnotationException(LuaExportClass.class));
            return false;
        }

        LuaTable classTable = new LuaTable();
        addConstructors(classTable, clazz);
        addStaticMethods(classTable, clazz);
        addStaticFields(classTable, clazz);
        setClassMetatable(classTable, clazz);
        sandbox.setTable(annotation.name(), classTable);
        return true;
    }

    public static boolean exportMethod(LuaTable root, Method method, Object instance) {
        Objects.requireNonNull(root);
        Objects.requireNonNull(method);
        Objects.requireNonNull(instance);

        LuaExportMethod annotation = method.getAnnotation(LuaExportMethod.class);
        if(annotation == null) return false;

        root.set(annotation.name().isEmpty()? method.getName() : annotation.name(), new MethodWrapper(annotation, method, instance));
        return true;
    }

    public static boolean exportField(LuaTable root, Field field, Object instance) {
        Objects.requireNonNull(root);
        Objects.requireNonNull(field);
        Objects.requireNonNull(instance);

        LuaExportField annotation = field.getAnnotation(LuaExportField.class);
        if(annotation == null) return false;

        root.set(annotation.name().isEmpty()? field.getName() : annotation.name(), new FieldWrapper(annotation, field, instance));
        return true;
    }

    private static void addConstructors(LuaTable classTable, Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            LuaExportMethod methodAnnotation = constructor.getAnnotation(LuaExportMethod.class);
            LuaExportConstructor constrAnnotation = constructor.getAnnotation(LuaExportConstructor.class);

            if (methodAnnotation != null || constrAnnotation != null ||
                    Modifier.isPublic(constructor.getModifiers())) {

                String name = "new";
                if (methodAnnotation != null && !methodAnnotation.name().isEmpty()) {
                    name = methodAnnotation.name();
                } else if (constrAnnotation != null && !constrAnnotation.name().isEmpty()) {
                    name = constrAnnotation.name();
                }

                classTable.set(name, createConstructorWrapper(constructor));
            }
        }
    }

    @Contract(value = "_ -> new", pure = true)
    private static @NotNull LuaValue createConstructorWrapper(Constructor<?> constructor) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                try {
                    Object[] javaArgs = convertConstructorArgs(constructor, args);
                    Object instance = constructor.newInstance(javaArgs);
                    return LuaConvertor.toLua(instance);
                } catch (Exception e) {
                    return LuaValue.NIL;
                }
            }
        };
    }

    private static Object[] convertConstructorArgs(Constructor<?> constructor, Varargs args) {
        Parameter[] parameters = constructor.getParameters();
        Object[] javaArgs = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (i < args.narg()) {
                javaArgs[i] = LuaConvertor.fromLua(args.arg(i + 1), parameters[i].getType());
            } else {
                javaArgs[i] = null;
            }
        }

        return javaArgs;
    }

    private static void addStaticMethods(LuaTable classTable, Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())) {
                LuaExportMethod annotation = method.getAnnotation(LuaExportMethod.class);
                String methodName = annotation != null && !annotation.name().isEmpty()
                        ? annotation.name() : method.getName();

                classTable.set(methodName, createStaticMethodWrapper(method, clazz));
            }
        }
    }

    private static LuaValue createStaticMethodWrapper(Method method, Class<?> clazz) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                try {
                    Object[] javaArgs = LuaConvertor.convertArgs(method, args);
                    Object result = method.invoke(null, javaArgs);
                    return LuaConvertor.toLua(result);
                } catch (Exception e) {
                    return LuaValue.NIL;
                }
            }
        };
    }

    private static void addStaticFields(LuaTable classTable, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                try {
                    classTable.set(field.getName(), LuaConvertor.toLua(field.get(null)));
                } catch (Exception ignored) {}
            }
        }
    }

    private static void setClassMetatable(LuaTable classTable, Class<?> clazz) {
        LuaTable metatable = new LuaTable();
        metatable.set(LuaValue.CALL, new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                try {
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    if (constructor != null && Modifier.isPublic(constructor.getModifiers())) {
                        Object instance = constructor.newInstance();
                        return LuaConvertor.toLua(instance);
                    }
                } catch (Exception e) {
                }
                return LuaValue.NIL;
            }
        });
        classTable.setmetatable(metatable);
    }
}
