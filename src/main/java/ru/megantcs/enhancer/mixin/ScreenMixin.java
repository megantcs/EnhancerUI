package ru.megantcs.enhancer.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.megantcs.enhancer.hook.ScreenRenderHook;
import ru.megantcs.enhancer.hook.data.ScreenRenderHookKeyPressedData;
import ru.megantcs.enhancer.hook.data.ScreenRenderHookRenderData;

@Mixin(Screen.class)
public class ScreenMixin
{
    @Inject(at = @At("TAIL"), method = "render")
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci)
    {
        Screen screen = (Screen) (Object) this;
        ScreenRenderHook.SCREEN_RENDER.emit(new ScreenRenderHookRenderData(screen, context, mouseX, mouseY, delta, ci));
    }

    @Inject(at = @At("TAIL"), method = "keyPressed", cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir)
    {
        Screen screen = (Screen) (Object) this;
        if(ScreenRenderHook.SCREEN_KEY_PRESSED.emit(new ScreenRenderHookKeyPressedData(screen, keyCode, scanCode, modifiers, cir)))
        {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
    @Inject(at = @At("TAIL"), method = "close", cancellable = true)
    public void close(CallbackInfo ci)
    {
        Screen screen = (Screen) (Object) this;
        if(ScreenRenderHook.SCREEN_CLOSE.emit(screen)) {
            ci.cancel();
        }
    }
}
