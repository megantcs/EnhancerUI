package ru.megantcs.enhancer.api.lua;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import ru.megantcs.enhancer.platform.toolkit.exceptions.ExceptionContainer;

import java.util.Objects;

public class LuaEnvironment
{
    final ExceptionContainer exceptionContainer;
    private final Globals globals;

    public LuaEnvironment(ExceptionContainer exceptionContainer) {
        this.exceptionContainer = Objects.requireNonNull(exceptionContainer);
        this.globals = JsePlatform.standardGlobals();
    }

    public boolean setTable(@Nullable String name, @Nullable LuaTable table)
    {
        if(name == null || table == null) return false;

        globals.set(name, table);
        return true;
    }

    public boolean loadScript(String code, String name)
    {
        Objects.requireNonNull(code);
        Objects.requireNonNull(name);

        try {
            var l = globals.load(code, name, globals);
            l.call();

            return true;
        } catch (Exception e) {
            exceptionContainer.add(LuaEnvironment.class, e);
        }
        return false;
    }

    public LuaValue getType(String name) {
        Objects.requireNonNull(name);
        return globals.get(name);
    }

    public @Nullable LuaValue getMethod(@NotNull String name) {
        var t = getType(name);
        if(t.isfunction()) return t;

        return null;
    }
}
