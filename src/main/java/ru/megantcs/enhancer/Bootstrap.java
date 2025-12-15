package ru.megantcs.enhancer;

import net.fabricmc.api.ModInitializer;
import ru.megantcs.enhancer.api.core.Enhancer;
import ru.megantcs.enhancer.impl.EnhancerInstance;

public class Bootstrap implements ModInitializer {

    @Override
    public void onInitialize()
    {
        Enhancer ui = EnhancerInstance.create();
    }
}
