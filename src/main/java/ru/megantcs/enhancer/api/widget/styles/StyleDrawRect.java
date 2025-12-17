package ru.megantcs.enhancer.platform.render.engine.widgets;

import ru.megantcs.enhancer.platform.render.engine.render.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.Colors.Brush;

@FunctionalInterface
public interface StyleDrawRect
{
    void drawRect(RenderObject renderObject, float x, float y, float z, float width, float height, float corner, Brush color);
}
