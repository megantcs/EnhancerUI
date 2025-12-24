package ru.megantcs.enhancer.impl.core;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import ru.megantcs.enhancer.api.core.DrawCallback;
import ru.megantcs.enhancer.api.core.Enhancer;
import ru.megantcs.enhancer.api.graphics.FontContainer;

public class EnhancerUI extends Enhancer
{
    private FontContainer fontContainer;

    public EnhancerUI() {
        HudRenderCallback.EVENT.register(this::hudRenderCallback);
    }

    private void hudRenderCallback(DrawContext context, float delta) {
        drawCallback.EVENT.invoker().draw(instanceObj(context.getMatrices()), delta);
    }

    @Override
    public RenderObject instanceObj(MatrixStack matrixStack)
    {
        return new RenderObject(matrixStack, fontContainer);
    }
}
