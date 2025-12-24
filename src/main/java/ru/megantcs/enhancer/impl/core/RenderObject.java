package ru.megantcs.enhancer.impl.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.AndMultipartModelSelector;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import ru.megantcs.enhancer.api.graphics.FontContainer;
import ru.megantcs.enhancer.api.graphics.FontRenderer;
import ru.megantcs.enhancer.api.graphics.GraphicsUtils;
import ru.megantcs.enhancer.platform.toolkit.colors.Brush;

import java.awt.*;
import java.util.Objects;

public class RenderObject
{
    public final MatrixStack matrixStack;
    protected final FontContainer fontContainer;

    public RenderObject(MatrixStack matrixStack, FontContainer fontContainer) {
        this.matrixStack = matrixStack;
        this.fontContainer = fontContainer;
    }

    public void drawGlow(float x, float y, float z, float width, float height, float radius, Brush brush)
    {
        Objects.requireNonNull(brush);
        GraphicsUtils.renderGlow(matrixStack, x, y, z, width, height, 1,1, radius,  brush);
    }

    public void drawRect(float x, float y, float z, float width, float height, float radius, float alpha, Brush brush) {
        Objects.requireNonNull(brush);
        GraphicsUtils.renderRectangle(matrixStack, x, y, z, width, height, radius, alpha, brush);
    }

    public void drawBlur(float x, float y, float z, float width, float height, float radius, float alpha, float blur, Brush brush) {
        Objects.requireNonNull(brush);
        GraphicsUtils.renderBlur(matrixStack, x, y, z, width, height, radius, brush.first(), alpha, blur);
    }

    public void drawLine(Vec3d start, Vec3d end, float widthLine, Brush brush) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);
        Objects.requireNonNull(brush);
        GraphicsUtils.renderLine(matrixStack, start, end, brush, widthLine);
    }

    public void drawBorder(float x, float y, float z, float width, float height, float cornerRadius, float border, float alpha, Brush brush) {
        Objects.requireNonNull(brush);
        GraphicsUtils.renderBorder(matrixStack, x, y, z, width, height, cornerRadius, border, alpha, brush);
    }

    public void drawText(float x, float y, String text, boolean shadow, Color c) {
        var color = c.getRGB();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

        try {
            MinecraftClient.getInstance().textRenderer.draw(Text.of(text), x, y, color, shadow, matrixStack.peek().getPositionMatrix(), immediate, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        }
        finally {
            immediate.draw();
        }
    }

    public void drawFont(FontRenderer font, float x, float y, float z, String text, Color c) {
        Objects.requireNonNull(font);
        Objects.requireNonNull(text);
        Objects.requireNonNull(c);

        font.drawString(matrixStack, text, x, y, z, c.getRGB());
    }

    public void drawContourBorder(float x, float y, float z, float width, float height, float corner, float thinness, float dash, float gap, Brush brush) {
        Objects.requireNonNull(brush);
        GraphicsUtils.renderContourBorder(matrixStack, x, y, z, width, height, corner, brush.first(), thinness, dash, gap);
    }
}
