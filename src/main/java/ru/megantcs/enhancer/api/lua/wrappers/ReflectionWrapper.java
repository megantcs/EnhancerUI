package ru.megantcs.enhancer.api.lua.wrappers;

import ru.megantcs.enhancer.api.lua.reflect.LuaExportClass;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportMethod;

import java.lang.reflect.*;

@LuaExportClass(name = "Reflection")
public class ReflectionWrapper {

    @LuaExportMethod
    public ReflectionWrapper() {}

    @LuaExportMethod
    public ClassWrapper forName(String className) {
        if (className == null || className.isEmpty()) {
            return null;
        }

        try {
            Class<?> clazz = Class.forName(className);
            return new ClassWrapper(clazz);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @LuaExportMethod
    public ClassWrapper getClass(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof ClassWrapper) {
            return (ClassWrapper) obj;
        }

        return new ClassWrapper(obj.getClass());
    }

    @LuaExportMethod
    public boolean isInstance(Object obj, String className) {
        if (obj == null || className == null) {
            return false;
        }

        try {
            Class<?> clazz = Class.forName(className);
            return clazz.isInstance(obj);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @LuaExportMethod
    public Object newInstance(String className, Object... args) {
        if (className == null) {
            return null;
        }

        try {
            Class<?> clazz = Class.forName(className);

            if (args == null || args.length == 0) {
                return clazz.getDeclaredConstructor().newInstance();
            }

            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }

            Constructor<?> constructor = clazz.getDeclaredConstructor(paramTypes);
            return constructor.newInstance(args);
        } catch (Exception e) {
            return null;
        }
    }

    @LuaExportMethod
    public Object invokeMethod(Object obj, String methodName, Object... args) {
        if (obj == null || methodName == null) {
            return null;
        }

        try {
            Class<?> clazz = obj.getClass();

            if (args == null || args.length == 0) {
                Method method = clazz.getMethod(methodName);
                return method.invoke(obj);
            }
            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }

            Method method = clazz.getMethod(methodName, paramTypes);
            return method.invoke(obj, args);
        } catch (Exception e) {
            return null;
        }
    }

    @LuaExportMethod
    public Object getFieldValue(Object obj, String fieldName) {
        if (obj == null || fieldName == null) {
            return null;
        }

        try {
            Class<?> clazz = obj.getClass();
            Field field = clazz.getField(fieldName);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    @LuaExportMethod
    public boolean setFieldValue(Object obj, String fieldName, Object value) {
        if (obj == null || fieldName == null) {
            return false;
        }

        try {
            Class<?> clazz = obj.getClass();
            Field field = clazz.getField(fieldName);
            field.set(obj, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @LuaExportMethod
    public String getJavaVersion() {
        return System.getProperty("java.version");
    }

    @LuaExportMethod
    public String toString() {
        return "ReflectionWrapper";
    }
}