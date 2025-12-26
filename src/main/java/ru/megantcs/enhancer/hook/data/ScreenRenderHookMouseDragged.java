package ru.megantcs.enhancer.hook.data;

import net.minecraft.client.gui.ParentElement;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public record ScreenRenderHookMouseDragged(ParentElement parentElement, double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {}
