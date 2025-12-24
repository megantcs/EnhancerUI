package ru.megantcs.enhancer;

import net.fabricmc.api.ModInitializer;
import ru.megantcs.enhancer.api.graphics.GraphicsUtils;
import ru.megantcs.enhancer.hook.handlers.HookHandlersManager;

public class Bootstrap implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        GraphicsUtils.init();
        HookHandlersManager.init();
    }
}
