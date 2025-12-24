package ru.megantcs.enhancer.api.graphics;

import net.minecraft.client.util.math.MatrixStack;

public interface FontRenderer
{
    void drawString(MatrixStack stack, String s, double x, double y, float z, int color);
    float getWidth(String text);
    float getHeight(String text);

    float getHeightLine();
}
