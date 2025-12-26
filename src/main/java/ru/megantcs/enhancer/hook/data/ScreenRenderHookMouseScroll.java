package ru.megantcs.enhancer.hook.data;

import net.minecraft.client.gui.ParentElement;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public record ScreenRenderHookMouseScroll(ParentElement parentElement, double mouseX, double mouseY, double amount, CallbackInfoReturnable<Boolean> cir) {}

