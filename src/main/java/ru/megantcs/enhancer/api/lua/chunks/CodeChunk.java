package ru.megantcs.enhancer.api.lua.chunks;

/**
 * implementation of a code chunk.
 * This chunk cannot be updated.
 * Therefore, the method implementation is ignored
 */
public class CodeChunk extends Chunk {

    public CodeChunk(String code, String name) {
        super(code, name);
    }

    @Override
    public CodeChunk updateValue() {return this;} // ignored
}
