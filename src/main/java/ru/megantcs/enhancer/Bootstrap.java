package ru.megantcs.enhancer;

import net.fabricmc.api.ModInitializer;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.api.graphics.GraphicsUtils;
import ru.megantcs.enhancer.hook.handlers.HookHandlersManager;
import ru.megantcs.enhancer.impl.core.LuaWrappers.RenderObjectSingleton;

public class Bootstrap implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        GraphicsUtils.init();
        HookHandlersManager.init();
        RenderObjectSingleton.init();
        LoggerFactory.getLogger("Enhancer-Bootstrap").info("Initialize");
    }
}
