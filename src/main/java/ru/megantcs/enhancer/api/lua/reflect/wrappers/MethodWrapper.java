package ru.megantcs.enhancer.api.lua.reflect.wrappers;

import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import ru.megantcs.enhancer.api.lua.LuaConvertor;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodWrapper extends Wrapper<LuaExportMethod, Method>
{
    public MethodWrapper(LuaExportMethod luaExportMethod, Method field, Object instance) {
        super(luaExportMethod, field, instance);
    }

    @Override
    public Varargs invoke(Varargs args) {
        Class<?>[] paramTypes = reflectType.getParameterTypes();
        Object[] javaArgs = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            if (i < args.narg()) {
                javaArgs[i] = LuaConvertor.fromLua(args.arg(i + 1), paramTypes[i]);
            }
        }

        Object result = null;
        try {
            result = reflectType.invoke(instance, javaArgs);
        } catch (IllegalAccessException | InvocationTargetException e)
        {
            logger.error("error invoke method: {}@{}",
                    reflectType.getName(), reflectType.toGenericString(), e);
        }
        return CoerceJavaToLua.coerce(result);
    }
}
