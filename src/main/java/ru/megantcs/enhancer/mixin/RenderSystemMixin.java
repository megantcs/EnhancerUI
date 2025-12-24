package ru.megantcs.enhancer.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.megantcs.enhancer.api.graphics.ImGuiLoader;

@Mixin(RenderSystem.class)
public class RenderSystemMixin
{
    @Inject(at = @At("HEAD"), method="flipFrame")
    private static void runTickTail(CallbackInfo ci)
    {
        ImGuiLoader.INSTANCE.renderFrame();
    }
}
