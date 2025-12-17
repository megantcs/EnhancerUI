package ru.megantcs.enhancer.platform.render.engine.widgets;

import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsContext;
import ru.megantcs.enhancer.platform.render.engine.render.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.Colors.Brush;
import ru.megantcs.enhancer.platform.toolkit.utils.Debug;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ComboBox extends WidgetCore
{
    final Set<String> options;
    final String title;

    private final List<ComboBoxItem> items;
    private String selected;
    private boolean dropdown = false;
    private float itemHeight = 20f;

    public ComboBox(String title, Set<String> options) {
        super(1, 15);
        this.options = options;
        this.title = title;

        this.items = new ArrayList<>();
        for (String option : options) {
            items.add(new ComboBoxItem(option));
        }

        updateItemsPositions();

        if (!items.isEmpty()) {
            this.selected = items.get(0).getTitle();
            items.get(0).setSelected(true);
        }
    }

    @Override
    protected void setup(RenderObject renderObject)
    {
        setSize(getWidthForOptions(title, options, renderObject) + 40, 15);
    }

    private int getWidthForOptions(String title, Set<String> options, RenderObject renderObject) {
        int maxWidth = getWidthStr(renderObject, title);
        for (String option : options) {
            int width = getWidthStr(renderObject, option);
            if (width > maxWidth) maxWidth = width;
        }
        return maxWidth + 40;
    }

    private void updateItemsPositions() {
        if (!dropdown) return;

        float startX = getX();
        float startY = getY() + getHeight();

        for (int i = 0; i < items.size(); i++) {
            ComboBoxItem item = items.get(i);
            float itemY = startY + (i * itemHeight);

            item.updatePosition(
                    startX,
                    itemY,
                    getWidth(),
                    itemHeight,
                    getWidth()
            );
        }
    }

    @Override
    protected void renderBackground(RenderObject renderObject)
    {
        drawRect(renderObject, getX(), getY(), getZIndex() - 1, getWidth(),
                 getHeight(), getCornerRadius(), getCurrentBackgroundColor());
    }

    @Override
    protected void renderContent(RenderObject renderObject, int mouseX, int mouseY, float delta)
    {
        if (dropdown) {
            updateItemsPositions();
        }

        String displayText = (selected != null && !selected.equals(title)) ? selected : title;

        int textWidth = Minecraft.mc.textRenderer.getWidth(displayText);
        float textX = getX() + (getWidth() - textWidth) / 2;
        float textY = getY() + (getHeight() - 9) / 2 + 1;

        Color textColor = isEnabled() ? Color.white : Color.gray;

        drawText(renderObject, textX, textY, displayText, Brush.of(textColor));

        if (dropdown) {
            renderDropdown(renderObject, mouseX, mouseY);
        }

        String arrow = dropdown ? "▲" : "▼";
        float arrowX = getX() + getWidth() - 20;
        float arrowY = getY() + (getHeight() - 9) / 2 + 1;

        drawText(renderObject, arrowX, arrowY, arrow, Brush.of(textColor));
    }

    private void renderDropdown(RenderObject renderObject, int mouseX, int mouseY) {
        updateItemsHoverState(mouseX, mouseY);

        for (ComboBoxItem item : items) {
            item.render(renderObject,
                    this::drawRect,
                    this::drawText,
                    getZIndex() + 10,
                    getCornerRadius(),
                    isEnabled(),
                    getBackgroundColor()
            );
        }
    }

    private void updateItemsHoverState(int mouseX, int mouseY) {
        for (ComboBoxItem item : items) {
            boolean isHovered = item.isHovered(mouseX, mouseY);
            item.setHovered(isHovered);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean result = super.mouseClicked(mouseX, mouseY, button);

        boolean clickedOnMainButton = mouseX >= getX() && mouseX <= getX() + getWidth() &&
                mouseY >= getY() && mouseY <= getY() + getHeight();

        if (clickedOnMainButton && button == 0) {
            dropdown = !dropdown;
            if (dropdown) {
                updateItemsPositions();
            }

            return true;
        }

        if (dropdown) {
            for (ComboBoxItem item : items)
            {
                if (item.isHovered((int)mouseX, (int)mouseY) && button == 0) {
                    setSelected(item);
                    dropdown = false;
                    return true;
                }
            }

            if (button == 0) {
                dropdown = false;
                return true;
            }
        }

        return result;
    }

    @Override
    protected void updateHoverState(int mouseX, int mouseY) {
        super.updateHoverState(mouseX, mouseY);
        if (dropdown) {
            updateItemsHoverState(mouseX, mouseY);
        }
    }

    private void setSelected(ComboBoxItem selectedItem) {
        for (ComboBoxItem item : items) {
            item.setSelected(false);
        }

        selectedItem.setSelected(true);
        this.selected = selectedItem.getTitle();
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String value) {
        if (options.contains(value)) {
            this.selected = value;
            for (ComboBoxItem item : items) {
                item.setSelected(item.getTitle().equals(value));
            }
        }
    }

    public int getSelectedIndex() {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getTitle().equals(selected)) {
                return i;
            }
        }
        return -1;
    }

    public void setSelectedIndex(int index) {
        if (index >= 0 && index < items.size()) {
            setSelected(items.get(index));
        }
    }

    public boolean isDropdownOpen() {
        return dropdown;
    }

    public void setDropdownOpen(boolean open) {
        this.dropdown = open;
        if (open) {
            updateItemsPositions();
        }
    }

    public void addOption(String option) {
        if (!options.contains(option)) {
            options.add(option);
            items.add(new ComboBoxItem(option));
        }
    }

    public void removeOption(String option) {
        options.remove(option);
        items.removeIf(item -> item.getTitle().equals(option));
        if (option.equals(selected)) {
            selected = items.isEmpty() ? title : items.get(0).getTitle();
            if (!items.isEmpty()) {
                items.get(0).setSelected(true);
            }
        }
    }

    public List<String> getOptions() {
        List<String> result = new ArrayList<>();
        for (ComboBoxItem item : items) {
            result.add(item.getTitle());
        }
        return result;
    }

    public ComboBox setItemHeight(float height) {
        this.itemHeight = Math.max(20, height);
        return this;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        if (dropdown) {
            updateItemsPositions();
        }
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        if (dropdown) {
            updateItemsPositions();
        }
    }

    public static class ComboBoxItem {
        private final String title;
        private float x;
        private float y;
        private float width;
        private float height;
        private float textX;
        private float textY;
        private boolean hovered = false;
        private boolean selected = false;

        public ComboBoxItem(String title) {
            this.title = title;
        }

        public void updatePosition(float x, float y, float width, float height, float parentWidth) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;

            int textWidth = Minecraft.mc.textRenderer.getWidth(title);
            this.textX = x + (parentWidth - textWidth) / 2;
            this.textY = y + (height - 9) / 2 + 1;
        }

        public String getTitle() {
            return title;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        public boolean isHovered() {
            return hovered;
        }

        public void setHovered(boolean hovered) {
            this.hovered = hovered;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public boolean isHovered(int mouseX, int mouseY) {
            return GraphicsContext.isHovered(mouseX, mouseY, x, y, width, height);
        }

        public void render(RenderObject renderObject, StyleDrawRect drawElement, StyleDrawText drawText, int zIndex, float cornerRadius,
                           boolean enabled, Color parentColor) {
            Color itemBgColor;
            if (hovered) {
                itemBgColor = new Color(
                        Math.min(255, parentColor.getRed() + 30),
                        Math.min(255, parentColor.getGreen() + 30),
                        Math.min(255, parentColor.getBlue() + 30),
                        parentColor.getAlpha()
                );
            } else if (selected) {
                itemBgColor = new Color(
                        Math.min(255, parentColor.getRed() + 20),
                        Math.min(255, parentColor.getGreen() + 20),
                        Math.min(255, parentColor.getBlue() + 50),
                        parentColor.getAlpha()
                );
            } else {
                itemBgColor = parentColor;
            }

            drawElement.drawRect(renderObject, x, y, zIndex, width, height, cornerRadius, Brush.of(itemBgColor));

            Color itemTextColor;
            if (!enabled) {
                itemTextColor = Color.gray;
            } else if (hovered) {
                itemTextColor = new Color(220, 220, 255);
            } else if (selected) {
                itemTextColor = Color.white;
            } else {
                itemTextColor = Color.white;
            }

            drawText.drawText(renderObject, textX, textY, title, Brush.of(itemTextColor));
        }
    }
}