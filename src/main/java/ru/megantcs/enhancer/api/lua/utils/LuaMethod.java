package ru.megantcs.enhancer.api.lua.utils;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import ru.megantcs.enhancer.api.lua.LuaConvertor;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportField;
import ru.megantcs.enhancer.platform.toolkit.reflect.AnnotationSearcher;

import java.lang.reflect.Field;
import java.util.Objects;

public class LuaMethod
{
    private final LuaValue value;

    public LuaMethod(LuaValue value) {
        Objects.requireNonNull(value);
        this.value = value;
        throwIfNotMethod();
    }

    private void throwIfNotMethod() {
        if(!value.isfunction()) {
           throw new IllegalArgumentException("value not is function");
        }
    }

    public void callArg(Object arg, String name) {
        LuaTable table = new LuaTable();
        table.set(name, LuaConvertor.toLua(arg));

        try {
            value.call(table);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void call(Object... arg)
    {
        LuaTable table = new LuaTable();
        for (Object o : arg) {
                var fields = AnnotationSearcher.getFields(o.getClass(), LuaExportField.class);
                for (Field field : fields) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    try {
                        table.set(field.getAnnotation(LuaExportField.class).name(), LuaConvertor.toLua(field.get(o)));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

        }
        try {
            value.call(table);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
