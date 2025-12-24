package ru.megantcs.enhancer.api.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import ru.megantcs.enhancer.api.lua.reflect.LuaTableCreator;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LuaConvertor
{
    public static Object[] convertArgs(Method method, Varargs args) {
        Parameter[] parameters = method.getParameters();
        Object[] javaArgs = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (i < args.narg()) {
                javaArgs[i] = fromLua(args.arg(i + 1), parameters[i].getType());
            } else {
                javaArgs[i] = null;
            }
        }

        return javaArgs;
    }
    public static LuaValue toLua(Object obj) {
        if (obj == null) return LuaValue.NIL;

        if (obj instanceof Integer) return LuaValue.valueOf((Integer) obj);
        if (obj instanceof Double) return LuaValue.valueOf((Double) obj);
        if (obj instanceof Float) return LuaValue.valueOf((Float) obj);
        if (obj instanceof Boolean) return LuaValue.valueOf((Boolean) obj);
        if (obj instanceof String) return LuaValue.valueOf((String) obj);
        if (obj instanceof Long) return LuaValue.valueOf((Long) obj);

        if (obj instanceof List) {
            LuaTable table = new LuaTable();
            List<?> list = (List<?>) obj;
            for (int i = 0; i < list.size(); i++) {
                table.set(i + 1, toLua(list.get(i)));
            }
            return table;
        }

        if (obj instanceof Map) {
            LuaTable table = new LuaTable();
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                table.set(toLua(entry.getKey()), toLua(entry.getValue()));
            }
            return table;
        }

        return LuaTableCreator.exportClassTable(obj, ExceptionFactory.createContainer());
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromLua(LuaValue value, Class<T> targetType) {
        if (value.isnil()) return null;

        if (targetType == Integer.class || targetType == int.class)
            return (T) Integer.valueOf(value.toint());
        if (targetType == Double.class || targetType == double.class)
            return (T) Double.valueOf(value.todouble());
        if (targetType == Float.class || targetType == float.class)
            return (T) Float.valueOf(value.tofloat());
        if (targetType == Boolean.class || targetType == boolean.class)
            return (T) Boolean.valueOf(value.toboolean());
        if (targetType == String.class)
            return (T) value.tojstring();
        if (targetType == Long.class || targetType == long.class)
            return (T) Long.valueOf(value.tolong());

        if (targetType == List.class && value.istable()) {
            List<Object> list = new ArrayList<>();
            LuaTable table = value.checktable();
            for (LuaValue key = table.next(LuaValue.NIL).arg1();
                 !key.isnil();
                 key = table.next(key).arg1()) {
                if (key.isint()) {
                    list.add(fromLua(table.get(key), Object.class));
                }
            }
            return (T) list;
        }

        return null;
    }
}
