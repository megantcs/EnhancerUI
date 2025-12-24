package ru.megantcs.enhancer.impl.widgets;

import ru.megantcs.enhancer.api.widget.Widget;
import ru.megantcs.enhancer.impl.core.RenderObject;

public class TextBlock extends Widget
{
    private String content;
    private boolean background;

    public TextBlock(String content) {
        super(0, 0);
        background = false;
        setContent(content);
    }

    private void update() {
        setSize(getWidthText(content) + (background? getPadding() : 0), 8);
    }

    public void setContent(String content) {
        this.content = content;
        update();
    }

    @Override
    protected void renderBackground(RenderObject renderObject)
    {
        if(!background) return;

        drawRect(renderObject, getX(), getY(), getZIndex() - 6, getWidth(), getHeight(), getBackgroundColor());
    }

    @Override
    protected void renderContent(RenderObject renderObject, int mouseX, int mouseY, float delta)
    {
        if(background) {
            var pos = calcCenterText(content);
            drawText(renderObject, pos.x, pos.y, zIndex + 1, content,  getCurrentTextColor());
        }
        else {
            drawText(renderObject, getX(), getY(), zIndex + 1, content, getCurrentTextColor());
        }
    }

    public boolean isBackground() {
        return background;
    }

    public TextBlock setBackground(boolean value) {
        background = value;
        update();
        return this;
    }
}
