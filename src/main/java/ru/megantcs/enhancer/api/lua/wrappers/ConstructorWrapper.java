package ru.megantcs.enhancer.api.lua.wrappers;

import ru.megantcs.enhancer.api.lua.reflect.LuaExportClass;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@LuaExportClass(name = "Constructor")
public class ConstructorWrapper {
    private final Constructor<?> constructor;

    @LuaExportMethod
    public ConstructorWrapper() {
        this.constructor = null;
    }

    public ConstructorWrapper(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @LuaExportMethod
    public String getName() {
        if (constructor == null) return "(null)";
        return constructor.getName();
    }

    @LuaExportMethod
    public int getParameterCount() {
        if (constructor == null) return 0;
        return constructor.getParameterCount();
    }

    @LuaExportMethod
    public String[] getParameterTypes() {
        if (constructor == null) return new String[0];
        Class<?>[] params = constructor.getParameterTypes();
        String[] paramNames = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            paramNames[i] = params[i].getSimpleName();
        }
        return paramNames;
    }

    @LuaExportMethod
    public String[] getFullParameterTypes() {
        if (constructor == null) return new String[0];
        Class<?>[] params = constructor.getParameterTypes();
        String[] paramNames = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            paramNames[i] = params[i].getName();
        }
        return paramNames;
    }

    @LuaExportMethod
    public boolean isPublic() {
        if (constructor == null) return false;
        return Modifier.isPublic(constructor.getModifiers());
    }

    @LuaExportMethod
    public boolean isPrivate() {
        if (constructor == null) return false;
        return Modifier.isPrivate(constructor.getModifiers());
    }

    @LuaExportMethod
    public boolean isProtected() {
        if (constructor == null) return false;
        return Modifier.isProtected(constructor.getModifiers());
    }

    @LuaExportMethod
    public String getModifiers() {
        if (constructor == null) return "";
        return Modifier.toString(constructor.getModifiers());
    }

    @LuaExportMethod
    public String getDeclaringClass() {
        if (constructor == null) return "";
        return constructor.getDeclaringClass().getName();
    }

    @LuaExportMethod
    public String toGenericString() {
        if (constructor == null) return "(null)";
        return constructor.toGenericString();
    }

    @LuaExportMethod
    public String toString() {
        if (constructor == null) return "ConstructorWrapper(null)";
        return "ConstructorWrapper[" + constructor.getDeclaringClass().getSimpleName() + "]";
    }

    public Constructor<?> getJavaConstructor() {
        return constructor;
    }
}