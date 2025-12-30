package ru.megantcs.enhancer.api.graphics;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import org.slf4j.Logger;
import ru.megantcs.enhancer.platform.toolkit.debugs.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@SuppressWarnings("all")
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
        Assert.throwExistFile(path);

        short[] buildBuffer = new short[] {start, end, 0};

        atlas.addFontFromFileTTF(path, fontSize, config, buildBuffer);
        return this;
    }

    public ImFontBuilder addFontFromFileTTF(String path, int fontSize)
    {
        Assert.throwExistFile(path);

        atlas.addFontFromFileTTF(path, fontSize, config);
        return this;
    }

    public ImFontBuilder addFontFromResourceTTF(Class<?> sender, String resourcePath, int fontSize, short start, short end) {
        try {
            InputStream fontStream = Assert.throwExistResource(sender, resourcePath);

            byte[] fontData = fontStream.readAllBytes();
            fontStream.close();

            short[] glyphRanges = new short[] {start, end, 0};
            atlas.addFontFromMemoryTTF(fontData, fontSize, config, glyphRanges);

            return this;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load font from resource: " + resourcePath, e);
        }
    }

    public ImFontBuilder addFontFromResourceTTF(Class<?> sender, String resourcePath, int fontSize) {
        try {
            InputStream fontStream = Assert.throwExistResource(sender, resourcePath);

            byte[] fontData = fontStream.readAllBytes();
            fontStream.close();

            atlas.addFontFromMemoryTTF(fontData, fontSize, config);

            return this;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load font from resource: " + resourcePath, e);
        }
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
