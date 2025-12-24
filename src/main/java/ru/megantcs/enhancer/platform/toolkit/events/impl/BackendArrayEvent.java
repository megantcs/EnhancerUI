package ru.megantcs.enhancer.platform.toolkit.events.impl;

import ru.megantcs.enhancer.platform.toolkit.events.api.Event;
import ru.megantcs.enhancer.platform.toolkit.interfaces.Func;
import ru.megantcs.enhancer.platform.toolkit.api.Unsupported;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

public class BackendArrayEvent<Invoker> extends Event<Invoker>
{
    private Func<Invoker[], Invoker> invokerHandler;
    private Set<Invoker> subscribes;

    final Class<Invoker> type;

    public BackendArrayEvent(Class<Invoker> type, Func<Invoker[], Invoker> invokerHandlers) {
        this.type = type;
        this.invokerHandler = invokerHandlers;
        subscribes = new HashSet<>();

        updateInvoker();
    }

    private void updateInvoker() {
        Invoker[] array = (Invoker[]) Array.newInstance(type, subscribes.size());
        array = subscribes.toArray(array);
        invoker = invokerHandler.run(array);
    }


    @Override
    public boolean register(Invoker listener) {
        if(subscribes.contains(listener)) return false;
        subscribes.add(listener);
        updateInvoker();

        return true;
    }

    @Override
    @Unsupported(reason = "use other events", since = "0.0.1")
    public boolean register(String name, Invoker invoker) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Unsupported(reason = "use other events", since = "0.0.1")
    public boolean unregister(Invoker invoker) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Unsupported(reason = "use other events", since = "0.0.1")
    public boolean unregister(String name) {
        throw new UnsupportedOperationException();
    }
}
