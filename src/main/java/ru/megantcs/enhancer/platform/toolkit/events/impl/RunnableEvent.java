package ru.megantcs.enhancer.platform.toolkit.events.impl;

import ru.megantcs.enhancer.platform.toolkit.events.api.Event;

import java.util.List;
import java.util.Objects;

public class RunnableEvent
        extends Event<Runnable>
{
    private final List<RunnableEventData> subscribes;

    public RunnableEvent(List<RunnableEventData> listType) {
        subscribes = listType;
        this.invoker = this::emit;
    }

    public void emit() {
        subscribes.forEach((e)->{
            e.subscribe().run();
        });
    }

    @Override
    public boolean register(Runnable runnable) {
        if(contains(runnable)) return false;

        subscribes.add(
                new RunnableEventData(uniqueName(runnable), runnable));
        return true;
    }

    public boolean contains(String name) {
        Objects.requireNonNull(name, "name cannot be null");

        for (RunnableEventData subscribe : subscribes) {
            if(Objects.equals(subscribe.name, name)) return true;
        }
        return false;
    }

    @Override
    public boolean register(String name, Runnable handler) {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(handler, "runnable cannot be null");

        if(contains(name) || contains(handler)) return false;

        subscribes.add(new RunnableEventData(name, handler));

        return true;
    }

    @Override
    public boolean unregister(Runnable runnable) {
        return subscribes.removeIf(e -> e.subscribe.equals(runnable));
    }

    @Override
    public boolean unregister(String name) {
        Objects.requireNonNull(name, "name cannot be null");

        return subscribes.removeIf((e)-> Objects.equals(e.name, name));
    }

    public boolean contains(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable cannot be null");

        for (RunnableEventData subscribe : subscribes) {
            if(Objects.equals(subscribe.subscribe, runnable)) return true;
        }
        return false;
    }

    private String uniqueName(Object obj) {
        return "runnable#event@" + obj.hashCode();
    }

    record RunnableEventData(String name, Runnable subscribe) {}
}
