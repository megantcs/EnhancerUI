package ru.megantcs.enhancer.impl.widgets;

import ru.megantcs.enhancer.api.widget.Widget;
import ru.megantcs.enhancer.impl.core.RenderObject;

import java.util.ArrayList;
import java.util.List;

public class StackPanel extends Widget
{
    private final List<Widget> widgets;
    private Orientation orientation;
    private float spacing;

    public StackPanel() {
        super(0, 0);
        widgets = new ArrayList<>();
        orientation = Orientation.HORIZONTAL;
        spacing = 10;
        setMinSize(50, 30);
    }

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    public void addWidget(Widget widgetCore) {
        if (widgetCore == null) return;
        widgets.add(widgetCore);
        addChild(widgetCore);
        updateLayout();
    }

    public void removeWidget(Widget widgetCore) {
        if (widgetCore == null) return;
        if (widgets.remove(widgetCore)) {
            removeChild(widgetCore);
            updateLayout();
        }
    }

    public void clearWidgets() {
        for (Widget widget : new ArrayList<>(widgets)) {
            removeWidget(widget);
        }
    }

    public List<Widget> getWidgets() {
        return new ArrayList<>(widgets);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        if (this.orientation != orientation) {
            this.orientation = orientation;
            updateLayout();
        }
    }

    public float getSpacing() {
        return spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = Math.max(0, spacing);
        updateLayout();
    }

    private void updateLayout() {
        if (widgets.isEmpty()) {
            setSize(getMinWidth(), getMinHeight());
            return;
        }

        if (orientation == Orientation.HORIZONTAL) {
            updateHorizontalLayout();
        } else {
            updateVerticalLayout();
        }
    }

    private void updateHorizontalLayout() {
        if (widgets.isEmpty()) {
            setSize(getMinWidth(), getMinHeight());
            return;
        }

        float totalWidth = 0;
        float maxHeight = 0;
        float currentX = 0;

        for (Widget widget : widgets) {
            if (!widget.isVisible()) continue;

            widget.setPosition(currentX, 0);

            totalWidth += widget.getWidth();
            currentX += widget.getWidth() + spacing;

            if (widget.getHeight() > maxHeight) {
                maxHeight = widget.getHeight();
            }
        }

        if (!widgets.isEmpty()) {
            totalWidth += (widgets.size() - 1) * spacing;
        }

        setSize(Math.max(totalWidth, getMinWidth()), Math.max(maxHeight, getMinHeight()));
    }

    private void updateVerticalLayout() {
        float x = 0;
        float y = 0;
        float maxWidth = 0;
        float totalHeight = 0;

        for (Widget widget : widgets) {
            if (!widget.isVisible()) continue;

            widget.setPosition(x, y);

            y += widget.getHeight() + spacing;
            totalHeight = y - spacing;

            if (widget.getWidth() > maxWidth) {
                maxWidth = widget.getWidth();
            }
        }

        setSize(Math.max(maxWidth, getMinWidth()), Math.max(totalHeight, getMinHeight()));
    }

    @Override
    public void render(RenderObject renderObject, int mouseX, int mouseY, float delta) {
        if (!isVisible()) return;

        renderBackground(renderObject);
        renderChildren(renderObject, mouseX, mouseY, delta);
        renderContent(renderObject, mouseX, mouseY, delta);
    }

    @Override
    protected void renderBackground(RenderObject renderObject) {
        drawWindowRect(renderObject, getX(), getY(), getZIndex() - 1,
                getWidth(), getHeight(), getGridColor());
    }

    @Override
    public void setPosition(float x, float y) {
        float dx = x - getX();
        float dy = y - getY();
        super.setPosition(x, y);

        for (Widget widget : widgets) {
            widget.move(dx, dy);
        }
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
    }
}