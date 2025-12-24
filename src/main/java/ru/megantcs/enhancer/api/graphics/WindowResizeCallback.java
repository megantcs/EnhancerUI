package ru.megantcs.enhancer.api.graphics;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import ru.megantcs.enhancer.platform.toolkit.events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.events.api.Event;

public interface WindowResizeCallback
{
    Event<WindowResizeCallback> EVENT_INVOKER = EventFactory.makeEvent(WindowResizeCallback.class, (windowResizeCallbacks ->
            (client, window) ->
    {
        for(var callback : windowResizeCallbacks)
            callback.onResized(client, window);
    }));

    void onResized(MinecraftClient client, Window window);
}
