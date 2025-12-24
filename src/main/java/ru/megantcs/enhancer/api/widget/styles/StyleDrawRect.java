package ru.megantcs.enhancer.api.widget.styles;

import ru.megantcs.enhancer.impl.core.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.colors.Brush;

@FunctionalInterface
public interface StyleDrawRect
{
    void drawRect(RenderObject renderObject, float x, float y, float z, float width, float height, Brush brush);
}
