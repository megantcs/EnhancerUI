package ru.megantcs.enhancer.hook;

import ru.megantcs.enhancer.hook.data.CrosshairRenderHookData;
import ru.megantcs.enhancer.platform.toolkit.events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.events.impl.FuncEvent;

public interface CrosshairRenderHook
{
    FuncEvent<CrosshairRenderHookData, Boolean> CROSSHAIR_RENDER = EventFactory.makeFuncEvent(false);
}
