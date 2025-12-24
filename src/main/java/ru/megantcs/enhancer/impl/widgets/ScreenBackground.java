package ru.megantcs.enhancer.impl.widgets;

import ru.megantcs.enhancer.api.widget.Widget;
import ru.megantcs.enhancer.impl.core.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.colors.Brush;

import java.awt.*;

public class ScreenBackground extends Widget
{
    private final Brush brush;

    public ScreenBackground(Brush brush) {
        super(0, 0);
        this.brush = brush;

        setSize(getWindowWidth(), getWindowHeight());
        setZIndex(-5);
    }

    @Override
    public void render(RenderObject renderObject, int mouseX, int mouseY, float delta) {
        renderObject.drawRect(getX(), getY(), zIndex-5, getWindowWidth(), getWindowHeight(), 0, 1, brush);
    }
}
