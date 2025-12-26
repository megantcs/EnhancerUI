package ru.megantcs.enhancer.hook.data;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public record ScreenRenderHookKeyPressedData(Screen screen, int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {}

