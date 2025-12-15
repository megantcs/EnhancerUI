package ru.megantcs.enhancer.platform.toolkit.events;

import ru.megantcs.enhancer.platform.toolkit.events.api.Event;
import ru.megantcs.enhancer.platform.toolkit.events.impl.BackendArrayEvent;
import ru.megantcs.enhancer.platform.toolkit.events.impl.RunnableEvent;
import ru.megantcs.enhancer.platform.toolkit.interfaces.Func;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventFactory
{
    public <T> Event<T> makeEvent(Class<T> type, Func<T[], T> invoker) {
        return new BackendArrayEvent<>(type, invoker);
    }

    public RunnableEvent makeRunnableEvent(List<RunnableEvent.RunnableEventData> listType) {
        return new RunnableEvent(listType);
    }

    public RunnableEvent makeRunnableEvent() {
        return new RunnableEvent(new ArrayList<>());
    }

    public RunnableEvent makeRunnableEventSync() {
        return makeRunnableEvent(new CopyOnWriteArrayList<>());
    }

    public RunnableEvent makeRunnableEventArray() {
        return new RunnableEvent(new ArrayList<>());
    }
}
