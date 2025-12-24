package ru.megantcs.enhancer.api.graphics;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ImFontBuilder
{
    private final ImFontAtlas atlas;
    private final Logger logger;
    private ImFontConfig config;

    public ImFontBuilder(ImFontAtlas atlas, Logger logger) {
        this.config = new ImFontConfig();

        this.logger = Objects.requireNonNull(logger);
        this.atlas = Objects.requireNonNull(atlas, "atlas");
    }

    public ImFontBuilder addFontFromFileTTF(String path, int fontSize)
    {
        if(!Files.exists(Path.of(path)))
            throw new RuntimeException("File not found: " + path);

        logger.info("Font loaded: " + path + ", size: " + fontSize);
        atlas.addFontFromFileTTF(path, fontSize, config);
        return this;
    }

    public ImFontBuilder setMergeMode(boolean value) {
        config.setMergeMode(value);
        return this;
    }

    public ImFontBuilder setGlyphMinAdvanceX(float value) {
        config.setGlyphMinAdvanceX(value);
        return this;
    }

    public ImFontBuilder newConfig() {
        config.destroy();
        config = new ImFontConfig();
        return this;
    }

    public ImFontBuilder addFontFromFileTTF(String path, int fontSize, short start, short end)
    {
        if(!Files.exists(Path.of(path)))
            throw new RuntimeException("File not found: " + path);

        short[] buildBuffer = new short[] {start, end, 0};
        logger.info("Font loaded: " + path + ", size: " + fontSize + ", fa_start: " + start + ", fa_end: " + end);

        atlas.addFontFromFileTTF(path, fontSize, config, buildBuffer);
        return this;
    }

    public ImFontBuilder build() {
        atlas.build();
        return this;
    }

    public ImFontBuilder clear() {
        atlas.clear();
        return this;
    }

    public ImFontBuilder addDefaultFont() {
        atlas.addFontDefault();
        return this;
    }

    public ImFontBuilder clearAndAddDefaultFont() {
        return clear().addDefaultFont();
    }
}
