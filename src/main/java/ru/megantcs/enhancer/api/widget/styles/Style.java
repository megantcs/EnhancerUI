package ru.megantcs.enhancer.platform.render.engine.widgets;
import ru.megantcs.enhancer.platform.render.engine.render.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.Colors.Brush;

import java.awt.*;

public abstract class Style {
    protected Brush backgroundColor = new Brush(new Color(40, 40, 40, 200));
    protected Brush borderColor = new Brush(new Color(100, 100, 100, 200));
    protected Brush textColor = new Brush(Color.WHITE);
    protected Brush hoverColor = new Brush(new Color(60, 60, 60, 200));
    protected Brush pressedColor = new Brush(new Color(80, 80, 80, 200));
    protected Brush disabledColor = new Brush(new Color(30, 30, 30, 150));
    protected Brush disabledTextColor = new Brush(new Color(150, 150, 150, 200));
    protected Brush focusBorderColor = new Brush(new Color(100, 150, 255, 255));

    protected float borderWidth = 1f;
    protected float cornerRadius = 3f;
    protected float padding = 5f;

    public Brush getBackgroundColor() { return backgroundColor; }
    public Brush getBorderColor() { return borderColor; }
    public Brush getTextColor() { return textColor; }
    public Brush getHoverColor() { return hoverColor; }
    public Brush getPressedColor() { return pressedColor; }
    public Brush getDisabledColor() { return disabledColor; }
    public Brush getDisabledTextColor() { return disabledTextColor; }
    public Brush getFocusBorderColor() { return focusBorderColor; }

    public float getBorderWidth() { return borderWidth; }
    public float getCornerRadius() { return cornerRadius; }
    public float getPadding() { return padding; }


    public Style setBackgroundColor(Brush brush) { this.backgroundColor = brush; return this; }
    public Style setBorderColor(Brush brush) { this.borderColor = brush; return this; }
    public Style setTextColor(Brush brush) { this.textColor = brush; return this; }
    public Style setHoverColor(Brush brush) { this.hoverColor = brush; return this; }
    public Style setPressedColor(Brush brush) { this.pressedColor = brush; return this; }
    public Style setDisabledColor(Brush brush) { this.disabledColor = brush; return this; }
    public Style setDisabledTextColor(Brush brush) { this.disabledTextColor = brush; return this; }
    public Style setFocusBorderColor(Brush brush) { this.focusBorderColor = brush; return this; }

    public Style setBorderWidth(float width) { this.borderWidth = Math.max(0, width); return this; }
    public Style setCornerRadius(float radius) { this.cornerRadius = Math.max(0, radius); return this; }
    public Style setPadding(float padding) { this.padding = Math.max(0, padding); return this; }

    public Brush getCurrentBackgroundColor(boolean enabled, boolean hovered, boolean pressed) {
        if (!enabled) return disabledColor;
        if (pressed) return pressedColor;
        if (hovered) return hoverColor;
        return backgroundColor;
    }

    public Brush getCurrentTextColor(boolean enabled) {
        if (!enabled) return disabledTextColor;
        return textColor;
    }

    public Brush getCurrentBorderColor(boolean enabled, boolean focused) {
        if (!enabled) return disabledColor;
        if (focused) return focusBorderColor;
        return borderColor;
    }

    public abstract void drawRect(RenderObject renderObject, float x, float y, float z, float width, float height, float corner, Brush color);
    public abstract void drawText(RenderObject renderObject, float x, float y, String text, Brush color);
    public abstract int getWidthText(RenderObject renderObject, String text);
}