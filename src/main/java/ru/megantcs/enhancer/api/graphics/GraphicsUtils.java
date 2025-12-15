package ru.megantcs.enhancer.api.graphics;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;

// minecraft graphics utils
public class GraphicsUtils
{
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


}
