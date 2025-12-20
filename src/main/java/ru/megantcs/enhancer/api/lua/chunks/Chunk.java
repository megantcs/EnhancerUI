package ru.megantcs.enhancer.api.lua;

import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.platform.toolkit.Warnings;
import ru.megantcs.enhancer.platform.toolkit.reflect.Noexcept;

import java.io.IOException;
import java.util.Objects;

public abstract class Chunk
{
    @NotNull
    private String code;

    @NotNull
    private final String name;

    public Chunk(String code, String name) {
        this.code = Objects.requireNonNull(code);
        this.name = Objects.requireNonNull(name);

        updateValue();
    }

    @Noexcept(access = IOException.class)
    public abstract void updateValue();

    final boolean executeChunk(LuaEnvironment environment) {
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