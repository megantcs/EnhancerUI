package ru.megantcs.enhancer.platform.render.engine.widgets;

import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.render.engine.math.Vec3d;
import ru.megantcs.enhancer.platform.render.engine.render.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.Colors.Brush;
import ru.megantcs.enhancer.platform.toolkit.utils.Debug;

import java.awt.*;
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
    }

    @Override
    public void drawRect(RenderObject renderObject, float x, float y, float z, float width, float height, float corner, Brush color)
    {
        renderObject.drawRect(new Vec3d(x, y, z), height, width, corner, 1, color);
    }

    @Override
    public void drawText(RenderObject renderObject, float x, float y, String text, Brush color) {
        Debug.warn("DS", String.join("   ", ""+x, ""+y, text, color.first().toString()));
        renderObject.drawMCFont(x, y, text, color.first(), false);
    }

    @Override
    public int getWidthText(RenderObject renderObject, String text) {
        return Minecraft.mc.textRenderer.getWidth(text);
    }
}