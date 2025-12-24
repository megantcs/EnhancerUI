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

public class ContourBorderShader {
    private Uniform2f uSize;
    private Uniform2f uLocation;
    private Uniform1f radius;
    private Uniform1f borderWidth;
    private Uniform1f dashSize;
    private Uniform1f gapSize;
    private Uniform4f borderColor;

    private MinecraftClient mc = MinecraftClient.getInstance();

    public static final ManagedCoreShader CONTOUR_BORDER_SHADER = ShaderEffectManager.getInstance()
            .manageCoreShader(new Identifier("enhancer", "contour_border"), VertexFormats.POSITION);

    public ContourBorderShader() {
        setup();
    }

    public void setParameters(float x, float y, float width, float height,
                              float cornerRadius, Color color,
                              float thickness, float dashLength, float gapLength) {
        float scaleFactor = (float) mc.getWindow().getScaleFactor();

        float minThickness = 2.0f;
        float minDash = 4.0f;
        float minGap = 2.0f;

        radius.set(Math.max(0.1f, cornerRadius * scaleFactor));
        borderWidth.set(Math.max(minThickness * scaleFactor, thickness * scaleFactor));
        dashSize.set(Math.max(minDash * scaleFactor, dashLength * scaleFactor));
        gapSize.set(Math.max(minGap * scaleFactor, gapLength * scaleFactor));

        borderColor.set(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                color.getAlpha() / 255f
        );

        uLocation.set(x * scaleFactor, -y * scaleFactor + mc.getWindow().getScaledHeight() * scaleFactor - height * scaleFactor);
        uSize.set(width * scaleFactor, height * scaleFactor);
    }

    public void use() {
        RenderSystem.setShader(CONTOUR_BORDER_SHADER::getProgram);
    }

    private void setup() {
        uSize = CONTOUR_BORDER_SHADER.findUniform2f("uSize");
        uLocation = CONTOUR_BORDER_SHADER.findUniform2f("uLocation");
        radius = CONTOUR_BORDER_SHADER.findUniform1f("radius");
        borderWidth = CONTOUR_BORDER_SHADER.findUniform1f("borderWidth");
        dashSize = CONTOUR_BORDER_SHADER.findUniform1f("dashSize");
        gapSize = CONTOUR_BORDER_SHADER.findUniform1f("gapSize");
        borderColor = CONTOUR_BORDER_SHADER.findUniform4f("borderColor");
    }
}