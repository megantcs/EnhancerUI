package ru.megantcs.enhancer.hook;

import ru.megantcs.enhancer.api.lua.toolkit.PosObject;
import ru.megantcs.enhancer.hook.data.HotbarRenderHookData;
import ru.megantcs.enhancer.platform.toolkit.events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.events.impl.FuncEvent;

public interface HotBarRenderHook
{
    FuncEvent<HotbarRenderHookData, Boolean> RENDER_BACKGROUND = EventFactory.makeFuncEvent(false);
    FuncEvent<PosObject, Boolean> RENDER_SELECT_SLOT = EventFactory.makeFuncEvent(false);
}
