package ru.megantcs.enhancer.api.graphics;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FontContainer
{
    private final Map<String, FontRenderer> fonts;

    public FontContainer() {
        fonts = new HashMap<>();
    }

    private FontRenderer createFontFromResourceTTF(String path, int size) throws IOException, FontFormatException {
        var font = GraphicsUtils.createFontFromResourceTTF(size, path);
        fonts.put(uniqueKey(path, size), font);

        return font;
    }

    public FontRenderer getFromResourceTTF(String path, int size) throws IOException, FontFormatException {
        if(fonts.containsKey(uniqueKey(path, size))) {
            return fonts.get(uniqueKey(path, size));
        }

        return createFontFromResourceTTF(path, size);
    }

    private String uniqueKey(String path, int size) {
        return "ttf@" + path + ":" + size;
    }
}
