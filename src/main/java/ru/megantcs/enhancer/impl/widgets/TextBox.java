package ru.megantcs.enhancer.impl.widgets;

import net.minecraft.client.gui.widget.TextFieldWidget;
import org.lwjgl.glfw.GLFW;
import ru.megantcs.enhancer.api.graphics.GraphicsMath;
import ru.megantcs.enhancer.api.widget.Widget;
import ru.megantcs.enhancer.impl.core.RenderObject;

import java.util.Set;

public class TextBox extends Widget
{
    private String text;

    public TextBox() {
        super(100, 100);
        text = "(null)";
    }

    @Override
    protected void renderBackground(RenderObject renderObject) {
        drawRect(renderObject, getX(), getY(), getZIndex(), getWidth(), getHeight(), getBackgroundColor());
    }

    @Override
    public void render(RenderObject renderObject, int mouseX, int mouseY, float delta)
    {
        var lines = text.split("\n");
        var y = 2;
        for (String line : lines)
        {
            drawText(renderObject, getX() + 3, y, getZIndex(), line, getCurrentTextColor());
            y += 2;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        var symbol = GraphicsMath.getCharFromKeyCode(keyCode, scanCode, modifiers);
        text += symbol;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
