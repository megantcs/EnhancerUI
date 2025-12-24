package ru.megantcs.enhancer.api.graphics.shaders;

import com.mojang.blaze3d.systems.RenderSystem;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import ladysnake.satin.api.managed.uniform.Uniform2f;
import ladysnake.satin.api.managed.uniform.Uniform4f;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import java.awt.*;

public class BorderShader {
    private Uniform2f uSize;
    private Uniform2f uLocation;
    private Uniform1f radius;
    private Uniform1f borderWidth;
    private Uniform1f borderAlpha;
    private Uniform4f color1;
    private Uniform4f color2;
    private Uniform4f color3;
    private Uniform4f color4;

    private MinecraftClient mc = MinecraftClient.getInstance();

    public static final ManagedCoreShader BORDER_SHADER = ShaderEffectManager.getInstance()
            .manageCoreShader(Identifier.of("enhancer", "border"), VertexFormats.POSITION);

    public BorderShader() {
        setup();
    }

    public void setParameters(float x, float y, float width, float height,
                              float cornerRadius, float borderThickness, float alpha,
                              Color topLeft, Color topRight, Color bottomRight, Color bottomLeft) {
        float scaleFactor = (float) mc.getWindow().getScaleFactor();

        radius.set(cornerRadius * scaleFactor);
        borderWidth.set(borderThickness * scaleFactor);
        borderAlpha.set(alpha);

        // Конвертируем координаты (Minecraft использует обратную Y-ось)
        uLocation.set(x * scaleFactor, -y * scaleFactor + mc.getWindow().getScaledHeight() * scaleFactor - height * scaleFactor);
        uSize.set(width * scaleFactor, height * scaleFactor);

        // Устанавливаем цвета градиента
        color1.set(topLeft.getRed() / 255f, topLeft.getGreen() / 255f, topLeft.getBlue() / 255f, alpha);
        color2.set(topRight.getRed() / 255f, topRight.getGreen() / 255f, topRight.getBlue() / 255f, alpha);
        color3.set(bottomRight.getRed() / 255f, bottomRight.getGreen() / 255f, bottomRight.getBlue() / 255f, alpha);
        color4.set(bottomLeft.getRed() / 255f, bottomLeft.getGreen() / 255f, bottomLeft.getBlue() / 255f, alpha);
    }

    // Перегрузка для одноцветной обводки
    public void setParameters(float x, float y, float width, float height,
                              float cornerRadius, float borderThickness, float alpha,
                              Color borderColor) {
        setParameters(x, y, width, height, cornerRadius, borderThickness, alpha,
                borderColor, borderColor, borderColor, borderColor);
    }

    public void use() {
        RenderSystem.setShader(BORDER_SHADER::getProgram);
    }

    private void setup() {
        uSize = BORDER_SHADER.findUniform2f("uSize");
        uLocation = BORDER_SHADER.findUniform2f("uLocation");
        radius = BORDER_SHADER.findUniform1f("radius");
        borderWidth = BORDER_SHADER.findUniform1f("borderWidth");
        borderAlpha = BORDER_SHADER.findUniform1f("borderAlpha");
        color1 = BORDER_SHADER.findUniform4f("color1");
        color2 = BORDER_SHADER.findUniform4f("color2");
        color3 = BORDER_SHADER.findUniform4f("color3");
        color4 = BORDER_SHADER.findUniform4f("color4");
    }
}