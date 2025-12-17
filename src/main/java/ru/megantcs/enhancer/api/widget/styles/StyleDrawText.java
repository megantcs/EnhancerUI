package ru.megantcs.enhancer.platform.render.engine.widgets;

import ru.megantcs.enhancer.platform.render.engine.render.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.Colors.Brush;

public interface StyleDrawText
{
    void drawText(RenderObject renderObject, float x, float y, String text, Brush color);
}
