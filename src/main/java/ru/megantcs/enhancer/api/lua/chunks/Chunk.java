package ru.megantcs.enhancer.api.lua.chunks;

import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.api.lua.LuaScriptEngine;
import ru.megantcs.enhancer.platform.toolkit.Warnings;
import ru.megantcs.enhancer.platform.toolkit.api.Noexcept;

import java.io.IOException;
import java.util.Objects;

/**
 * an abstract chunk class that can update its value.
 * The chunk update is used when LuaSandBox is restarted
 * @see ru.megantcs.enhancer.api.lua.LuaSandBox
 */
@SuppressWarnings("all")
public abstract class Chunk
{
    @NotNull private String code;
    @NotNull private final String name;

    public Chunk(String code, String name) {
        this.code = Objects.requireNonNull(code);
        this.name = Objects.requireNonNull(name);
    }

    @Noexcept(access = IOException.class)
    public abstract Chunk updateValue();

    public final boolean executeChunk(LuaScriptEngine environment) {
        return environment.loadScript(code, name);
    }

    @SuppressWarnings(Warnings.unusedReturnValue)
    protected final boolean setCode(@NotNull String code) {
        if(this.code.equals(code)) return false;

        this.code = Objects.requireNonNull(code);
        return true;
    }

    public boolean equals(Chunk o)
    {
        Objects.requireNonNull(o);
        return unique().equals(o.unique());
    }

    protected final String unique() {
        return name + "$" + code;
    }
}