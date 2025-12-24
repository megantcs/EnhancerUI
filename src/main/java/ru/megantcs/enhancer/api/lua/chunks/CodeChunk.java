package ru.megantcs.enhancer.api.lua.chunks;

public class CodeChunk extends Chunk {

    public CodeChunk(String code, String name) {
        super(code, name);
    }

    @Override
    public CodeChunk updateValue() {return this;} // ignored
}
