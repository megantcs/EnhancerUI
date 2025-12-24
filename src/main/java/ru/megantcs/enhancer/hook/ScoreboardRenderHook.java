package ru.megantcs.enhancer.hook;

import net.minecraft.client.gui.DrawContext;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportField;
import ru.megantcs.enhancer.platform.toolkit.events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.events.impl.FuncEvent;

public interface ScoreboardRenderHook
{
    FuncEvent<RenderInfo, Boolean> RENDER_BACKGROUND = EventFactory.makeFuncEvent(false);
    FuncEvent<RenderInfo, Boolean> RENDER_SEPARATOR = EventFactory.makeFuncEvent(false);
    FuncEvent<RenderInfo, Boolean> RENDER_HEADER = EventFactory.makeFuncEvent(false);
    FuncEvent<RenderInfo, Boolean> RENDER_END = EventFactory.makeFuncEvent(false);

    public static record RenderInfo(DrawContext context,
                                    @LuaExportField(name = "left") int left,
                                    @LuaExportField(name = "top") int top,
                                    @LuaExportField(name = "right") int right,
                                    @LuaExportField(name = "bottom") int bottom,
                                    @LuaExportField(name = "rgb") int color) {}
}
