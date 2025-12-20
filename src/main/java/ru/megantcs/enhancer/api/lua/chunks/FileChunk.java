package ru.megantcs.enhancer.api.lua.chunks;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileLuaChunk extends Chunk
{
    @NotNull
    private final Path path;

    public FileLuaChunk(String code, String name, String path) {
        super(code, name);
        this.path =
                Path.of(Objects.requireNonNull(path));
    }

    @Override
    public void updateValue()
    {
        try {
            setCode(Files.readString(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
