package ru.megantcs.enhancer.api.core;

import ru.megantcs.enhancer.impl.core.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.events.api.Event;

public class DrawCallback
{
    public Event<IDrawCallback> EVENT = EventFactory.makeEvent(IDrawCallback.class, (e)->(renderObject, delta) -> {
       for(var t : e) t.draw(renderObject, delta);
    });

    public static interface IDrawCallback {
        public void draw(RenderObject renderObject, float delta);
    }
}
