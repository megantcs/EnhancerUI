package ru.megantcs.enhancer.api.lua.reflect.wrappers;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import ru.megantcs.enhancer.api.lua.LuaConvertor;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportField;

import java.lang.reflect.Field;

public class FieldWrapper extends Wrapper<LuaExportField, Field>
{
    public FieldWrapper(LuaExportField luaExportField, Field field, Object instance) {
        super(luaExportField, field, instance);
    }

    @Override
    public Varargs invoke(Varargs args) {
        try {
            if (args.narg() == 0 && annotation.read()) {
                Object value = reflectType.get(instance);
                return CoerceJavaToLua.coerce(value);
            } else if (args.narg() == 1 && annotation.write()) {
                Object value = LuaConvertor.fromLua(args.arg(1), reflectType.getType());
                reflectType.set(instance, value);
                return LuaValue.NIL;
            } else {
                return LuaValue.NIL;
            }
        } catch (Exception e) {
            return LuaValue.NIL;
        }
    }
}
