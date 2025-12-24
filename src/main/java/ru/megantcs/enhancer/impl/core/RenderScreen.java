package ru.megantcs.enhancer.impl.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import ru.megantcs.enhancer.api.core.Enhancer;
import ru.megantcs.enhancer.api.widget.Widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RenderScreen extends Screen
{
    protected final Enhancer enhancer;
    protected final List<Widget> widgets = new ArrayList<>();

    protected RenderScreen(Text title, Enhancer enhancer) {
        super(title);
        this.enhancer = enhancer;
        setup();
    }

    protected void setup() {}
    protected void shutdown() {}

    @Override
    public final void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        widgets.sort(Comparator.comparingInt(Widget::getZIndex));

        renderCore(enhancer.instanceObj(context.getMatrices()), mouseX, mouseY, delta);
    }

    private void renderCore(RenderObject object, int mouseX, int mouseY, float delta) {
        for (Widget widget : widgets) {
            if (widget.isVisible()) {
                widget.render(object, mouseX, mouseY, delta);
            }
        }
        render(object, mouseX, mouseY, delta);
    }

    protected void render(RenderObject renderObject, int mouseX, int mouseY, float delta) {}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            widget.mouseClicked(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean handled = false;
        for (Widget widget : widgets) {
            if (widget.isVisible() && widget.isEnabled()) {
                widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }

        return handled || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean handled = false;
        for (Widget widget : widgets) {
            if (widget.isVisible() && widget.isEnabled()) {
                widget.mouseReleased(mouseX, mouseY, button);
            }
        }

        return handled || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);

            if (widget.isVisible() && widget.isEnabled() && widget.isMouseOver((int)mouseX, (int)mouseY)) {
                if (widget.mouseScrolled(mouseX, mouseY, amount)) {
                    //return true;
                }
            }
        }

        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Widget widget : widgets) {
            if (widget.isVisible() && widget.isEnabled() && widget.isFocused()) {
                if (widget.keyPressed(keyCode, scanCode, modifiers)) {
                    //return true;
                }
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (Widget widget : widgets) {
            if (widget.isVisible() && widget.isEnabled() && widget.isFocused()) {
                if (widget.keyReleased(keyCode, scanCode, modifiers)) {
                    //return true;
                }
            }
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (Widget widget : widgets) {
            if (widget.isVisible() && widget.isEnabled() && widget.isFocused()) {
                if (widget.charTyped(chr, modifiers)) {
                    return true;
                }
            }
        }

        return super.charTyped(chr, modifiers);
    }

    protected boolean addWidget(Widget widget) {
        if(widgets.contains(widget)) return false;

        widgets.add(widget);
        return true;
    }

    protected boolean addWidget(Widget widgetCore, int x, int y) {
        if(addWidget(widgetCore)) {
            widgetCore.setPosition(x, y);
            return true;
        }
        return false;
    }

    protected boolean removeWidget(Widget widget) {
        return widgets.remove(widget);
    }

    protected void clearWidgets() {
        widgets.clear();
    }

    protected Widget getWidget(int index) {
        return widgets.get(index);
    }

    protected List<Widget> getWidgets() {
        return Collections.unmodifiableList(widgets);
    }

    @Override
    public void close() {
        for (Widget widget : widgets) {
            widget.setFocus(false);
        }
        super.close();
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
}
