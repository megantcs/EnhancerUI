package ru.megantcs.enhancer.mixin;

import net.minecraft.client.gui.ParentElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.megantcs.enhancer.hook.ScreenRenderHook;
import ru.megantcs.enhancer.hook.data.*;

@Mixin(ParentElement.class)
public interface ParentElementMixin extends ParentElement
{
    @Inject(at = @At("TAIL"), method = "mouseClicked", cancellable = true)
    default void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir)
    {
        var screen = (ParentElement) (Object) this;

        if(ScreenRenderHook.SCREEN_MOUSE_CLICKED.emit(new ScreenRenderHookMouseClicked(screen, mouseX, mouseY, button, cir))) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "mouseDragged", cancellable = true)
    default void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {
        var screen = (ParentElement) (Object) this;

        if(ScreenRenderHook.SCREEN_MOUSE_DRAGGED.emit(new ScreenRenderHookMouseDragged(
                screen, mouseX, mouseY, button, deltaX, deltaY, cir
        ))) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "mouseScrolled", cancellable = true)
    default void mouseScrolled(double mouseX, double mouseY, double amount, CallbackInfoReturnable<Boolean> cir) {
        var screen = (ParentElement) (Object) this;

        if(ScreenRenderHook.SCREEN_MOUSE_SCROLL.emit(new ScreenRenderHookMouseScroll(
                screen, mouseX, mouseY, amount, cir
        ))) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "keyReleased", cancellable = true)
    default void keyReleased(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        var screen = (ParentElement) (Object) this;

        if(ScreenRenderHook.SCREEN_KEY_RELEASED.emit(new ScreenRenderHookKeyReleased(
                screen, keyCode, scanCode, modifiers, cir
        ))) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "keyReleased", cancellable = true)
    default void charTyped(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        var screen = (ParentElement) (Object) this;

        if(ScreenRenderHook.SCREEN_CHAR_TYPED.emit(new ScreenRenderHookCharTyped(
                screen, keyCode, scanCode, modifiers, cir
        ))) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
