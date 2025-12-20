package ru.megantcs.enhancer.api.lua.chunks;

public class LChunk extends Chunk {

    public LChunk(String code, String name) {
        super(code, name);
    }

    @Override
    public void updateValue() {} // ignored
}
