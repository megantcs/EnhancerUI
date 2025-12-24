package ru.megantcs.enhancer.api.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.api.graphics.FontRenderer;
import ru.megantcs.enhancer.api.graphics.GraphicsMath;
import ru.megantcs.enhancer.api.widget.styles.DefaultStyle;
import ru.megantcs.enhancer.api.widget.styles.Style;
import ru.megantcs.enhancer.impl.core.RenderObject;
import ru.megantcs.enhancer.impl.core.RenderScreen;
import ru.megantcs.enhancer.platform.toolkit.colors.Brush;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Widget
{
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    private float width;
    private float height;
    private float x;
    private float y;

    protected boolean visible = true;
    protected boolean enabled = true;
    protected boolean hovered = false;
    protected boolean pressed = false;
    protected boolean focused = false;

    protected Style style = new DefaultStyle();

    protected int zIndex = 0;
    protected Widget parent = null;
    protected final List<Widget> children = new ArrayList<>();

    protected boolean draggable = false;
    protected boolean resizable = false;
    private boolean dragging = false;
    private boolean resizing = false;
    private float dragOffsetX, dragOffsetY;
    private float resizeStartX, resizeStartY;
    protected float minWidth = 20f;
    protected float minHeight = 20f;

    private boolean initialize = false;

    public Logger logger = LoggerFactory.getLogger(getClass());

    public Widget(float width, float height) {
        this.width = Math.max(minWidth, width);
        this.height = Math.max(minHeight, height);
        this.x = 0;
        this.y = 0;
    }

    public float getMinHeight() {
        return minHeight;
    }

    public float getMinWidth() {
        return minWidth;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void render(RenderObject renderObject, int mouseX, int mouseY, float delta) {
        if (!visible) return;
        if(!initialize) {
            initialize = true;
            setup(renderObject);
        }

        // debug
        // renderObject.drawBorder(getX(), getY(), 5, getWidth(), getHeight(),2, 1, 1, Brush.of(Color.red) );

        updateHoverState(mouseX, mouseY);
        renderBackground(renderObject);
        renderBorder(renderObject);
        renderContent(renderObject, mouseX, mouseY, delta);
        renderChildren(renderObject, mouseX, mouseY, delta);
    }

    protected void renderChildren(RenderObject renderObject, int mouseX, int mouseY, float delta) {
        children.sort(Comparator.comparingInt(Widget::getZIndex));

        for (Widget child : children) {
            if (child.isVisible()) {
                child.render(renderObject, mouseX, mouseY, delta);
            }
        }
    }

    protected void setup(RenderObject renderObject) {}
    protected void renderBackground(RenderObject renderObject) {}
    protected void renderBorder(RenderObject renderObject) {}
    protected void renderContent(RenderObject renderObject, int mouseX, int mouseY, float delta) {}

    protected void updateHoverState(int mouseX, int mouseY) {
        hovered = isMouseOver(mouseX, mouseY) && enabled && visible;

        for (Widget child : children) child.updateHoverState(mouseX, mouseY);
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return GraphicsMath.isHovered(mouseX, mouseY, x, y, width, height);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!visible || !enabled) return false;

        boolean handled = false;

        List<Widget> sortedChildren = new ArrayList<>(children);
        sortedChildren.sort(Comparator.comparingInt(Widget::getZIndex).reversed());

        for (Widget child : sortedChildren) {
            if (child.mouseClicked(mouseX, mouseY, button)) {
                handled = true;
            }
        }

        if (!handled) {
            boolean overWidget = isMouseOverDirect((int)mouseX, (int)mouseY);

            if (overWidget) {
                focused = true;
                pressed = true;

                if (draggable) {
                    dragging = true;
                    dragOffsetX = (float)(mouseX - getAbsoluteX());
                    dragOffsetY = (float)(mouseY - getAbsoluteY());
                }

                onMouseClick((int)mouseX, (int)mouseY, button);
                handled = true;
            } else {
                setFocus(false);
            }
        }

        return handled;
    }

    private boolean isMouseOverDirect(int mouseX, int mouseY) {
        return mouseX >= getAbsoluteX() && mouseX <= getAbsoluteX() + width &&
                mouseY >= getAbsoluteY() && mouseY <= getAbsoluteY() + height;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!visible || !enabled) return false;

        boolean handled = false;

        for (Widget child : children) {
            if (child.mouseReleased(mouseX, mouseY, button)) {
                handled = true;
            }
        }

        boolean wasPressed = pressed;
        pressed = false;
        dragging = false;
        resizing = false;

        if (wasPressed) {
            onMouseRelease((int)mouseX, (int)mouseY, button);
            handled = true;
        }

        return handled;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!visible || !enabled) return false;

        boolean handled = false;

        for (Widget child : children) {
            if (child.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                handled = true;
            }
        }

        if (dragging && draggable) {
            setPosition((float)mouseX - dragOffsetX, (float)mouseY - dragOffsetY);
            onDrag((float)mouseX, (float)mouseY);
            handled = true;
        }

        if (resizing && resizable) {
            float deltaWidth = (float)mouseX - resizeStartX;
            float deltaHeight = (float)mouseY - resizeStartY;

            if (Math.abs(deltaWidth) > 1 || Math.abs(deltaHeight) > 1) {
                width = Math.max(minWidth, width + deltaWidth);
                height = Math.max(minHeight, height + deltaHeight);
                resizeStartX = (float)mouseX;
                resizeStartY = (float)mouseY;
                onResize(width, height);
                handled = true;
            }
        }

        return handled;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!visible || !enabled) return false;

        boolean handled = false;

        List<Widget> sortedChildren = new ArrayList<>(children);
        sortedChildren.sort(Comparator.comparingInt(Widget::getZIndex).reversed());

        for (Widget child : sortedChildren) {
            child.mouseScrolled(mouseX, mouseY, amount);
        }

        if (isMouseOver((int)mouseX, (int)mouseY)) {
            onMouseScroll((int)mouseX, (int)mouseY, amount);
            handled = true;
        }

        return handled;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!visible || !enabled) return false;

        boolean handled = false;

        for (Widget child : children) {
            child.keyPressed(keyCode, scanCode, modifiers);
        }

        if (focused) {
            onKeyPress(keyCode, scanCode, modifiers);
        }

        return handled;
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (!visible || !enabled) return false;

        boolean handled = false;

        for (Widget child : children) {
            child.keyReleased(keyCode, scanCode, modifiers);
        }

        if (!handled && focused) {
            onKeyRelease(keyCode, scanCode, modifiers);
            handled = true;
        }

        return handled;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!visible || !enabled) return false;

        boolean handled = false;

        for (Widget child : children) {
            if (child.isFocused() && child.charTyped(chr, modifiers)) {
                handled = true;
                break;
            }
        }

        if (!handled && focused) {
            onCharTyped(chr, modifiers);
            handled = true;
        }

        return handled;
    }

    protected void onMouseClick(int mouseX, int mouseY, int button) {}
    protected void onMouseRelease(int mouseX, int mouseY, int button) {}
    protected void onDrag(float mouseX, float mouseY) {}
    protected void onResize(float newWidth, float newHeight) {}
    protected void onMouseScroll(int mouseX, int mouseY, double amount) {}
    protected void onKeyPress(int keyCode, int scanCode, int modifiers) {}
    protected void onKeyRelease(int keyCode, int scanCode, int modifiers) {}
    protected void onCharTyped(char chr, int modifiers) {}
    protected void onFocusGained() {}
    protected void onFocusLost() {}

    protected void onChildAdded(Widget child) {}
    protected void onChildRemoved(Widget child) {}

    private boolean isMouseOverResizeHandle(int mouseX, int mouseY) {
        float absX = getAbsoluteX();
        float absY = getAbsoluteY();
        return mouseX >= absX + width - 8 && mouseX <= absX + width &&
                mouseY >= absY + height - 8 && mouseY <= absY + height;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;

        for (Widget child : children) {
            child.move(dx, dy);
        }
    }

    public void setSize(float width, float height) {
        this.width = Math.max(minWidth, width);
        this.height = Math.max(minHeight, height);
    }

    public void resize(float dw, float dh) {
        this.width = Math.max(minWidth, width + dw);
        this.height = Math.max(minHeight, height + dh);
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getCenterX() { return x + (width / 2); }
    public float getCenterY() { return y + (height / 2); }

    public boolean isVisible() { return visible; }
    public boolean isEnabled() { return enabled; }
    public boolean isHovered() { return hovered; }
    public boolean isPressed() { return pressed; }
    public boolean isFocused() { return focused; }
    public boolean isDraggable() { return draggable; }
    public boolean isResizable() { return resizable; }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (!visible) {
            focused = false;
            pressed = false;
            for (Widget child : children) {
                child.setVisible(false);
            }
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            pressed = false;
            focused = false;

            for (Widget child : children) {
                child.setEnabled(false);
            }
        }
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
        if (!draggable) dragging = false;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        if (!resizable) resizing = false;
    }

    public void setMinSize(float minWidth, float minHeight) {
        this.minWidth = Math.max(10, minWidth);
        this.minHeight = Math.max(10, minHeight);
        if (width < this.minWidth) width = this.minWidth;
        if (height < this.minHeight) height = this.minHeight;
    }

    public void setFocus(boolean focus) {
        if (this.focused != focus) {
            this.focused = focus;
            if (focus) {
                onFocusGained();
                for (Widget child : children) {
                    child.setFocus(false);
                }
            } else {
                onFocusLost();
            }
        }
    }

    public void setStyle(Style style) {
        this.style = style != null ? style : new DefaultStyle();
    }

    public Style getStyle() {
        return style;
    }

    public Widget setCornerRadius(float radius) {
        style.setCornerRadius(radius);
        return this;
    }

    public float getCornerRadius() {
        return style.getCornerRadius();
    }

    public void setPadding(float padding) {
        style.setPadding(padding);
    }

    public float getPadding() {
        return style.getPadding();
    }

    protected Brush getCurrentBackgroundColor() {
        return style.getCurrentBackgroundColor(enabled, hovered, pressed);
    }

    protected Brush getCurrentTextColor() {
        return style.getCurrentTextColor(enabled);
    }

    protected Brush getCurrentBorderColor() {
        return style.getCurrentBorderColor(enabled, focused);
    }

    public Brush getBackgroundColor() {
        return style.getBackgroundColor();
    }

    protected Color getBorderColor() {
        return style.getBorderColor().color1();
    }

    public void setZIndex(int zIndex) { this.zIndex = zIndex; }
    public int getZIndex() { return zIndex; }

    public void setParent(Widget parent) {
        if (this.parent != null) {
            this.parent.removeChild(this);
        }
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
    }
    public Widget getParent() { return parent; }

    public float getAbsoluteX() {
        if (parent != null) return parent.getAbsoluteX() + x;
        return x;
    }

    public float getAbsoluteY() {
        if (parent != null) return parent.getAbsoluteY() + y;
        return y;
    }

    public void centerHorizontally(float containerWidth) {
        this.x = (containerWidth - width) / 2;
    }

    public void centerVertically(float containerHeight) {
        this.y = (containerHeight - height) / 2;
    }

    public void center(float containerWidth, float containerHeight) {
        centerHorizontally(containerWidth);
        centerVertically(containerHeight);
    }

    public boolean containsPoint(float pointX, float pointY) {
        return pointX >= getAbsoluteX() && pointX <= getAbsoluteX() + width &&
                pointY >= getAbsoluteY() && pointY <= getAbsoluteY() + height;
    }

    protected FontRenderer getFont() {
        return style.getFont();
    }

    protected void drawRect(RenderObject renderObject, float x, float y, float z, float width, float height, Brush brush) {
        style.drawRect(renderObject, x, y, z, width, height, brush);
    }

    protected void execute(Runnable runnable) {
        MinecraftClient.getInstance().execute(() -> {
            try {
                if (runnable == null) return;
                runnable.run();
            } catch (Exception exception) {
                logger.error("error execute runnable.", exception);
            }
        });
    }

    public boolean addChild(Widget child) {
        if (child == this || children.contains(child)) {
            return false;
        }

        children.add(child);
        child.parent = this;
        onChildAdded(child);
        return true;
    }

    public boolean removeChild(Widget child) {
        if (children.remove(child)) {
            child.parent = null;
            onChildRemoved(child);
            return true;
        }
        return false;
    }

    public void clearChildren() {
        for (Widget child : new ArrayList<>(children)) {
            removeChild(child);
        }
    }

    public List<Widget> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public Widget getChild(int index) {
        return children.get(index);
    }

    protected Brush getGridColor() {
        return style.getGridColor();
    }

    public Widget getChildAt(float x, float y) {
        List<Widget> sortedChildren = new ArrayList<>(children);
        sortedChildren.sort(Comparator.comparingInt(Widget::getZIndex).reversed());

        for (Widget child : sortedChildren) {
            if (child.isVisible() && child.containsPoint(x, y)) {
                return child;
            }
        }
        return null;
    }

    public void setChildrenZIndex(int zIndex) {
        for (Widget child : children) {
            child.setZIndex(zIndex);
        }
    }

    public void setChildrenVisible(boolean visible) {
        for (Widget child : children) {
            child.setVisible(visible);
        }
    }

    public void setChildrenEnabled(boolean enabled) {
        for (Widget child : children) {
            child.setEnabled(enabled);
        }
    }

    public int getChildCount() {
        return children.size();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public void drawWindowRect(RenderObject renderObject, float x, float y, float z, float width, float height, Brush brush)  {
        style.drawWindowRect(renderObject, x, y, z, width, height, brush);
    }

    protected float getWidthText(String text) {
        return getFont().getWidth(text);
    }

    protected float getHeightText(String text) {
        return getFont().getHeight(text);
    }
    protected void setScreen(RenderScreen renderScreen) {
        execute(()->MinecraftClient.getInstance().setScreen(renderScreen));
    }
    protected void drawText(RenderObject renderObject, float x, float y, float z, String text, Brush color) {
        Objects.requireNonNull(renderObject);
        Objects.requireNonNull(text);
        Objects.requireNonNull(color);
        getFont().drawString(renderObject.matrixStack, text, x, y, z, color.first().getRGB());
    }

    protected Vec2f calcCenterText(String text) {
        var textWidth = getWidthText(text);
        float textX = getX() + (getWidth() - textWidth) / 2;
        float textY = getY() + (getHeight() - getFont().getHeightLine()) / 2 + 3;

        return new Vec2f(textX, textY);
    }

    protected int getWindowWidth() {
        return MinecraftClient.getInstance().getWindow().getWidth();
    }

    protected int getWindowHeight() {
        return MinecraftClient.getInstance().getWindow().getHeight();
    }

    protected int getWindowCenterX() {
        return getWindowWidth() / 2;
    }

    protected int getWindowCenterY() {
        return getWindowHeight() / 2;
    }

    protected float getWindowCenterXF() {
        return getWindowWidth() / 2.0f;
    }

    protected float getWindowCenterYF() {
        return getWindowHeight() / 2.0f;
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment)
    {
        switch (horizontalAlignment)
        {
            case LEFT -> {
                setPosition(0, getY());
            }
            case CENTER -> {
                setPosition((getWindowWidth() - width) / 2.0f, getY());
            }
            case RIGHT -> {
                setPosition(getWindowWidth() - width, getY());
            }
        }
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment)
    {
        switch (verticalAlignment) {
            case TOP -> {
                setPosition(getX(), 0);
            }
            case BOTTOM -> {
                setPosition(getX(), getWindowHeight() - height);
            }
            case CENTER -> {
                setPosition(getX(), (getWindowHeight() - height) / 2.0f);
            }
        }
    }
}