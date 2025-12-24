package ru.megantcs.enhancer.impl.widgets;

import ru.megantcs.enhancer.api.widget.Widget;
import ru.megantcs.enhancer.impl.core.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.colors.Brush;

import java.awt.*;

public class CheckBox extends Widget
{
    private boolean enabled;
    private float offset;
    private String title;

    public CheckBox(boolean enabled, String title) {
        super(15, 15);
        this.enabled = enabled;
        this.title = title;
        this.offset = 8;
    }

    @Override
    protected void renderBackground(RenderObject renderObject) {
        drawRect(renderObject, getX(), getY(), zIndex, getWidth(), getHeight(), getCurrentBackgroundColor());
    }

    @Override
    protected void renderContent(RenderObject renderObject, int mouseX, int mouseY, float delta) {
        var symbol = enabled? "On" : "Off";
        var color = enabled? Color.green : Color.red;
        var pos = calcCenterText(symbol);

        drawText(renderObject, pos.x, pos.y, zIndex + 1, symbol, Brush.of(color));
        drawText(renderObject,  getX() + getWidth() + offset, pos.y, zIndex + 1, title, getCurrentTextColor());
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!isHovered()) return false;

        if(button == 0) enabled = !enabled;
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
