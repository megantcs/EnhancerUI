package ru.megantcs.enhancer.api.widget.styles;

import ru.megantcs.enhancer.api.graphics.GraphicsUtils;
import ru.megantcs.enhancer.impl.core.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.colors.Brush;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class DefaultStyle extends Style
{
    public DefaultStyle() {
        this.backgroundColor = new Brush(new Color(29, 29, 29, 255));
        this.borderColor = new Brush(new Color(90, 90, 90, 220));
        this.hoverColor = new Brush(new Color(65, 65, 65, 220));
        this.pressedColor = new Brush(new Color(75, 75, 75, 220));
        this.cornerRadius = 4f;
        this.borderWidth = 1.5f;
        try {
            this.font = GraphicsUtils.createFontFromResourceTTF(16,"mc");
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawRect(RenderObject renderObject, float x, float y, float z, float width, float height, Brush brush)
    {
        renderObject.drawRect(x, y, z, width, height, cornerRadius, 1, brush);
    }

    @Override
    public void drawWindowRect(RenderObject renderObject, float x, float y, float z, float width, float height, Brush brush) {
        // shadow
        renderObject.drawGlow(x, y, z - 1, width, height, cornerRadius, brush);
        renderObject.drawRect(x, y, z, width, height, cornerRadius, 1, brush);
    }
}