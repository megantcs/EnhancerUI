package ru.megantcs.enhancer.api.lua.wrappers;

import org.jetbrains.annotations.Nullable;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportClass;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportMethod;

import java.lang.reflect.*;

@LuaExportClass(name = "Method")
public class MethodWrapper {
    private final Method method;

    @LuaExportMethod
    public MethodWrapper() {
        this.method = null;
    }

    public MethodWrapper(Method method) {
        this.method = method;
    }

    @LuaExportMethod
    public String getName() {
        if (method == null) return "(null)";
        return method.getName();
    }

    @LuaExportMethod
    public String getReturnType() {
        if (method == null) return "void";
        return method.getReturnType().getSimpleName();
    }

    @LuaExportMethod
    public String getFullReturnType() {
        if (method == null) return "void";
        return method.getReturnType().getName();
    }

    @LuaExportMethod
    public int getParameterCount() {
        if (method == null) return 0;
        return method.getParameterCount();
    }

    @LuaExportMethod
    public String[] getParameterTypes() {
        if (method == null) return new String[0];
        Class<?>[] params = method.getParameterTypes();
        String[] paramNames = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            paramNames[i] = params[i].getSimpleName();
        }
        return paramNames;
    }

    @LuaExportMethod
    public String[] getFullParameterTypes() {
        if (method == null) return new String[0];
        Class<?>[] params = method.getParameterTypes();
        String[] paramNames = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            paramNames[i] = params[i].getName();
        }
        return paramNames;
    }

    @LuaExportMethod
    public boolean isPublic() {
        if (method == null) return false;
        return Modifier.isPublic(method.getModifiers());
    }

    @LuaExportMethod
    public boolean isPrivate() {
        if (method == null) return false;
        return Modifier.isPrivate(method.getModifiers());
    }

    @LuaExportMethod
    public boolean isProtected() {
        if (method == null) return false;
        return Modifier.isProtected(method.getModifiers());
    }

    @LuaExportMethod
    public boolean isStatic() {
        if (method == null) return false;
        return Modifier.isStatic(method.getModifiers());
    }

    @LuaExportMethod
    public boolean isFinal() {
        if (method == null) return false;
        return Modifier.isFinal(method.getModifiers());
    }

    @LuaExportMethod
    public String getModifiers() {
        if (method == null) return "";
        return Modifier.toString(method.getModifiers());
    }

    @LuaExportMethod
    public String getDeclaringClass() {
        if (method == null) return "";
        return method.getDeclaringClass().getName();
    }

    @LuaExportMethod
    public String toGenericString() {
        if (method == null) return "(null)";
        return method.toGenericString();
    }

    @LuaExportMethod
    public String toString() {
        if (method == null) return "MethodWrapper(null)";
        return "MethodWrapper[" + method.getName() + "]";
    }

    public Method getJavaMethod() {
        return method;
    }
}