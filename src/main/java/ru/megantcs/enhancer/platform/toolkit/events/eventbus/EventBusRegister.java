package ru.megantcs.enhancer.platform.toolkit.events.eventbus;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import ru.megantcs.enhancer.api.graphics.ImGuiLoader;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBusRegister
{
    private static final EventBusRegister INSTANCE = new EventBusRegister();
    private final List<EventHandler> events;

    public EventBusRegister() {
        events = new CopyOnWriteArrayList<>();
        init();
    }

    public static EventBusRegister getInstance()
    {
        return INSTANCE;
    }

    private void init() {
        ClientTickEvents.END_CLIENT_TICK.register((mc)->{
            events.forEach((e)->e.onGameTick(mc));
        });
        HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> {
            events.forEach((e)->e.onHudRender(drawContext, tickDelta));
        }));
        WorldRenderEvents.AFTER_ENTITIES.register((world)->{
            events.forEach((e)->e.onWorldRender(world));
        });
        ImGuiLoader.INSTANCE.RENDER_FRAME_EVENT.register((io)->{
            events.forEach((e) -> e.imguiRender(io));
        });
    }

    public void register(EventHandler eventHandler)
    {
        Objects.requireNonNull(eventHandler);
        events.add(eventHandler);
    }

    public boolean unregister(EventHandler eventHandler) {
        Objects.requireNonNull(eventHandler);
        return events.remove(eventHandler);
    }
}