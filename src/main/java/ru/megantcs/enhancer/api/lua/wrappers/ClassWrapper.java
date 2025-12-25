package ru.megantcs.enhancer.api.lua.wrappers;

import ru.megantcs.enhancer.api.lua.reflect.LuaExportClass;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportMethod;

import java.lang.reflect.*;

@LuaExportClass(name = "Class")
public class ClassWrapper {
    private final Class<?> clazz;

    @LuaExportMethod
    public ClassWrapper() {
        this.clazz = null;
    }

    public ClassWrapper(Class<?> clazz) {
        this.clazz = clazz;
    }

    @LuaExportMethod
    public String getName() {
        if (clazz == null) return "(null)";
        return clazz.getName();
    }

    @LuaExportMethod
    public String getSimpleName() {
        if (clazz == null) return "(null)";
        return clazz.getSimpleName();
    }

    @LuaExportMethod
    public String getPackageName() {
        if (clazz == null) return "";
        Package pkg = clazz.getPackage();
        return pkg != null ? pkg.getName() : "";
    }

    @LuaExportMethod
    public boolean isInterface() {
        if (clazz == null) return false;
        return clazz.isInterface();
    }

    @LuaExportMethod
    public boolean isEnum() {
        if (clazz == null) return false;
        return clazz.isEnum();
    }

    @LuaExportMethod
    public boolean isArray() {
        if (clazz == null) return false;
        return clazz.isArray();
    }

    @LuaExportMethod
    public boolean isPrimitive() {
        if (clazz == null) return false;
        return clazz.isPrimitive();
    }

    @LuaExportMethod
    public String getSuperclass() {
        if (clazz == null) return "";
        Class<?> superclass = clazz.getSuperclass();
        return superclass != null ? superclass.getName() : "";
    }

    @LuaExportMethod
    public String[] getInterfaces() {
        if (clazz == null) return new String[0];
        Class<?>[] interfaces = clazz.getInterfaces();
        String[] interfaceNames = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            interfaceNames[i] = interfaces[i].getName();
        }
        return interfaceNames;
    }

    @LuaExportMethod
    public MethodWrapper[] getMethods() {
        if (clazz == null) return new MethodWrapper[0];
        Method[] methods = clazz.getMethods();
        MethodWrapper[] wrappers = new MethodWrapper[methods.length];
        for (int i = 0; i < methods.length; i++) {
            wrappers[i] = new MethodWrapper(methods[i]);
        }
        return wrappers;
    }

    @LuaExportMethod
    public MethodWrapper[] getDeclaredMethods() {
        if (clazz == null) return new MethodWrapper[0];
        Method[] methods = clazz.getDeclaredMethods();
        MethodWrapper[] wrappers = new MethodWrapper[methods.length];
        for (int i = 0; i < methods.length; i++) {
            wrappers[i] = new MethodWrapper(methods[i]);
        }
        return wrappers;
    }

    @LuaExportMethod
    public FieldWrapper[] getFields() {
        if (clazz == null) return new FieldWrapper[0];
        Field[] fields = clazz.getFields();
        FieldWrapper[] wrappers = new FieldWrapper[fields.length];
        for (int i = 0; i < fields.length; i++) {
            wrappers[i] = new FieldWrapper(fields[i]);
        }
        return wrappers;
    }

    @LuaExportMethod
    public FieldWrapper[] getDeclaredFields() {
        if (clazz == null) return new FieldWrapper[0];
        Field[] fields = clazz.getDeclaredFields();
        FieldWrapper[] wrappers = new FieldWrapper[fields.length];
        for (int i = 0; i < fields.length; i++) {
            wrappers[i] = new FieldWrapper(fields[i]);
        }
        return wrappers;
    }

    @LuaExportMethod
    public ConstructorWrapper[] getConstructors() {
        if (clazz == null) return new ConstructorWrapper[0];
        Constructor<?>[] constructors = clazz.getConstructors();
        ConstructorWrapper[] wrappers = new ConstructorWrapper[constructors.length];
        for (int i = 0; i < constructors.length; i++) {
            wrappers[i] = new ConstructorWrapper(constructors[i]);
        }
        return wrappers;
    }

    @LuaExportMethod
    public ConstructorWrapper[] getDeclaredConstructors() {
        if (clazz == null) return new ConstructorWrapper[0];
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        ConstructorWrapper[] wrappers = new ConstructorWrapper[constructors.length];
        for (int i = 0; i < constructors.length; i++) {
            wrappers[i] = new ConstructorWrapper(constructors[i]);
        }
        return wrappers;
    }

    @LuaExportMethod
    public MethodWrapper getMethod(String name, String... parameterTypes) {
        if (clazz == null || name == null) return null;

        try {
            if (parameterTypes == null || parameterTypes.length == 0) {
                Method method = clazz.getMethod(name);
                return new MethodWrapper(method);
            } else {
                Class<?>[] paramClasses = new Class<?>[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    paramClasses[i] = Class.forName(parameterTypes[i]);
                }
                Method method = clazz.getMethod(name, paramClasses);
                return new MethodWrapper(method);
            }
        } catch (Exception e) {
            return null;
        }
    }

    @LuaExportMethod
    public FieldWrapper getField(String name) {
        if (clazz == null || name == null) return null;

        try {
            Field field = clazz.getField(name);
            return new FieldWrapper(field);
        } catch (Exception e) {
            return null;
        }
    }

    @LuaExportMethod
    public String toString() {
        if (clazz == null) return "ClassWrapper(null)";
        return "ClassWrapper[" + clazz.getName() + "]";
    }

    public Class<?> getJavaClass() {
        return clazz;
    }
}