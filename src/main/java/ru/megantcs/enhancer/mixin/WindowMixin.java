package ru.megantcs.enhancer.mixin;

import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.megantcs.enhancer.api.graphics.ImGuiLoader;

@Mixin(Window.class)
public class WindowMixin
{
    @Shadow
    @Final
    private long handle;

    @Inject(at = @At("TAIL"),method = "<init>",remap = false)
    private void init(WindowEventHandler windowEventHandler, MonitorTracker screenManager, WindowSettings displayData, String string, String string2, CallbackInfo ci)
    {
        ImGuiLoader.INSTANCE.initWindow(handle);
    }
}
