package ru.megantcs.enhancer.api.widget.styles;

import ru.megantcs.enhancer.impl.core.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.colors.Brush;

public interface StyleDrawText
{
    void drawText(RenderObject renderObject, float x, float y, float z, String text, Brush color);
}
