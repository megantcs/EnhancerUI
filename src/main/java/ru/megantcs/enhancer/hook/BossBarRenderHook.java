package ru.megantcs.enhancer.hook;

import net.minecraft.client.gui.DrawContext;
import ru.megantcs.enhancer.platform.toolkit.events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.events.impl.FuncEvent;

public interface BossBarRenderHook
{
    FuncEvent<BossBarRenderData, Boolean> RENDER_PROGRESS = EventFactory.makeFuncEvent(false);
    FuncEvent<BossBarRenderData, Boolean> RENDER_BACKGROUND = EventFactory.makeFuncEvent(false);

    public static record BossBarRenderData(DrawContext context, float x, float y, float width, float height) {}
}
