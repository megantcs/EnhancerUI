package ru.megantcs.enhancer.hook.data;

import net.minecraft.client.gui.ParentElement;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public record ScreenRenderHookMouseClicked(ParentElement parentElement, double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {}
