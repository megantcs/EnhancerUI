package ru.megantcs.enhancer.hook.data;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record ScreenRenderHookRenderData(Screen screen, DrawContext drawContext, int mouseX, int mouseY, float delta, CallbackInfo ci) {}
