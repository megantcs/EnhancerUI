package ru.megantcs.enhancer.impl.widgets;

import ru.megantcs.enhancer.api.widget.Widget;
import ru.megantcs.enhancer.impl.core.RenderObject;

public class TextButton extends Widget
{
    private final String content;
    private final Runnable action;

    public TextButton(String content, Runnable action) {
        super(0, 20);
        this.content = content;
        this.action = action;

        setSize(getWidthText(content) + (8 * 2), 20);
    }

    @Override
    protected void renderBackground(RenderObject renderObject)
    {
        renderObject.drawRect(getX(), getY(), getZIndex(), getWidth(), getHeight(), getCornerRadius(), 1, getCurrentBackgroundColor());
    }

    @Override
    protected void renderContent(RenderObject renderObject, int mouseX, int mouseY, float delta)
    {
        var pos = calcCenterText(content);
        drawText(renderObject, pos.x, pos.y, zIndex + 1, content, getCurrentTextColor());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!isHovered()) return false;

        if(button == 0) execute(action);
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
