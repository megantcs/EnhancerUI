package ru.megantcs.enhancer.platform.toolkit.events.api;

public abstract class Event<Invoker>
{
    protected volatile Invoker invoker;

    public Event() {}

    public final Invoker invoker() { return invoker; }

    public abstract boolean register(Invoker invoker);
}
