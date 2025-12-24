package ru.megantcs.enhancer.api.graphics;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import ru.megantcs.enhancer.api.graphics.shaders.*;
import ru.megantcs.enhancer.platform.toolkit.colors.Brush;
import ru.megantcs.enhancer.platform.toolkit.colors.BrushHelper;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

// minecraft graphics utils
public class GraphicsUtils
{
    private static RectangleShader RECTANGLE_SHADER;
    private static GlowShader GLOW_SHADER;
    private static BlurShader BLUR_SHADER;
    private static BorderShader BORDER_SHADER;
    private static ContourBorderShader CONTOUR_BORDER_SHADER;

    public static void setupRender() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public static void resetRender() {
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public static BufferBuilder instanceBuffer() {
        return Tessellator.getInstance().getBuffer();
    }

    public static void draw()
    {
        Tessellator.getInstance().draw();
    }


    public static BufferBuilder prepareShaderDraw(MatrixStack matrices, float x, float y, float z, float width, float height) {
        setupRender();
        BufferBuilder buffer = instanceBuffer();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        setRectanglePoints(buffer, matrix, x, y, z, x + width, y + height);

        return buffer;
    }

    public static void setRectanglePoints(BufferBuilder buffer, Matrix4f matrix, float x, float y, float z, float x1, float y1) {
        buffer.vertex(matrix, x, y, z).next();
        buffer.vertex(matrix, x, y1, z).next();
        buffer.vertex(matrix, x1, y1, z).next();
        buffer.vertex(matrix, x1, y, z).next();
    }

    public static void renderRectangle(MatrixStack matrixStack, float x, float y, float z, float width, float height, float radius, float alpha,
                                       Brush brush) {
        var colors = BrushHelper.fillArray(brush, 4);
        var bb = prepareShaderDraw(matrixStack, x, y, z, width, height);

        RECTANGLE_SHADER.setParameters(x, y, width, height, radius, alpha, colors[0], colors[1], colors[2], colors[3]);
        RECTANGLE_SHADER.use();

        BufferRenderer.drawWithGlobalProgram(bb.end());
        resetRender();
    }

    public static void renderBorder(MatrixStack matrixStack, float x, float y, float z,
                                    float width, float height, float cornerRadius,
                                    float borderThickness, float alpha, Brush brush) {
        setupRender();

        var colors = BrushHelper.fillArray(brush, 4);

        BORDER_SHADER.setParameters(x, y, width, height, cornerRadius, borderThickness, alpha,
                colors[0], colors[1], colors[2], colors[3]);
        BORDER_SHADER.use();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        float padding = borderThickness * 0.5f;
        buffer.vertex(matrix, x - padding, y + height + padding, z).next();
        buffer.vertex(matrix, x + width + padding, y + height + padding, z).next();
        buffer.vertex(matrix, x + width + padding, y - padding, z).next();
        buffer.vertex(matrix, x - padding, y - padding, z).next();

        tessellator.draw();
        resetRender();
    }

    public static void renderGlow(MatrixStack matrixStack, float x, float y, float z, float width, float height, float glow, float outline, float radius, Brush brush) {
        var colors = BrushHelper.fillArray(brush, 4);
        BufferBuilder bb = prepareShaderDraw(matrixStack, x - 10, y - 10, z, width + 20, height + 20);
        GLOW_SHADER.setParameters(x, y, width, height, radius, 1, glow, outline, radius, colors[0], colors[1], colors[2], colors[3]);
        GLOW_SHADER.use();
        BufferRenderer.drawWithGlobalProgram(bb.end());
        resetRender();
    }

    public static void renderBlur(MatrixStack matrices, float x, float y, float z, float width, float height, float radius, Color c1, float blurStrenth, float blurOpacity) {
        BufferBuilder bb = prepareShaderDraw(matrices, x,y, z, width, height);
        BLUR_SHADER.setParameters(x, y, width, height, radius, c1, blurStrenth, blurOpacity);
        BLUR_SHADER.use();

        BufferRenderer.drawWithGlobalProgram(bb.end());
        resetRender();
    }

    public static void renderLine(MatrixStack matrixStack, Vec3d pos, Vec3d endPos, Brush brush, float lineWidth) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        var colors = BrushHelper.fillArray(brush, 2);
        int first = colors[0].getRGB();
        int second = colors[1].getRGB();

        RenderSystem.lineWidth(lineWidth);

        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z)
                .color(first)
                .next();

        buffer.vertex(matrix, (float) endPos.x, (float) endPos.y, (float) endPos.z)
                .color(second)
                .next();

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    public static void renderContourBorder(MatrixStack matrixStack, float x, float y, float z,
                                           float width, float height, float cornerRadius,
                                           Color color, float thickness,
                                           float dashLength, float gapLength) {
        setupRender();

        CONTOUR_BORDER_SHADER.setParameters(x, y, width, height, cornerRadius,
                color, thickness, dashLength, gapLength);

        CONTOUR_BORDER_SHADER.use();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        float padding = Math.max(2.0f, thickness);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        buffer.vertex(matrix, x - padding, y + height + padding, z).next();
        buffer.vertex(matrix, x + width + padding, y + height + padding, z).next();
        buffer.vertex(matrix, x + width + padding, y - padding, z).next();
        buffer.vertex(matrix, x - padding, y - padding, z).next();

        tessellator.draw();
        resetRender();
    }

    public static @NotNull FontRenderer createFontFromResourceTTF(float size, String name) throws IOException, FontFormatException {
        return new GraphicsFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(GraphicsFontRenderer.class.getClassLoader().getResourceAsStream("assets/enhancer/fonts/" + name + ".ttf"))).deriveFont(Font.PLAIN, size / 2f), size / 2f);
    }

    public static FontRenderer createFontFromTTF(float size, String path) throws IOException, FontFormatException {
        File fontFile = new File(path);
        if (!fontFile.exists()) {
            throw new FileNotFoundException("Font file not found: " + path);
        }

        Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        Font sizedFont = baseFont.deriveFont(Font.PLAIN, size / 2f);

        return new GraphicsFontRenderer(sizedFont, size / 2f);
    }

    public static void init()
    {
        CONTOUR_BORDER_SHADER
                = new ContourBorderShader();

        RECTANGLE_SHADER
                = new RectangleShader();

        GLOW_SHADER
                = new GlowShader();

        BLUR_SHADER
                = new BlurShader();

        BORDER_SHADER
                = new BorderShader();
    }
}
