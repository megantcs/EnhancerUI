package ru.megantcs.enhancer.hook.data;

import net.minecraft.client.gui.ParentElement;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public record ScreenRenderHookCharTyped(ParentElement parentElement, int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {}

