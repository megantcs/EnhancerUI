package ru.megantcs.enhancer.hook;

import net.minecraft.client.gui.DrawContext;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportField;
import ru.megantcs.enhancer.hook.data.ScoreboardRenderHookData;
import ru.megantcs.enhancer.platform.toolkit.events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.events.impl.FuncEvent;

public interface ScoreboardRenderHook
{
    FuncEvent<ScoreboardRenderHookData, Boolean> RENDER_BACKGROUND = EventFactory.makeFuncEvent(false);
    FuncEvent<ScoreboardRenderHookData, Boolean> RENDER_SEPARATOR = EventFactory.makeFuncEvent(false);
    FuncEvent<ScoreboardRenderHookData, Boolean> RENDER_HEADER = EventFactory.makeFuncEvent(false);
    FuncEvent<ScoreboardRenderHookData, Boolean> RENDER_END = EventFactory.makeFuncEvent(false);
}
