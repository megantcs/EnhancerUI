package ru.megantcs.enhancer.platform.toolkit.events.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.toolkit.Warnings;

/**
 * purpose: create/use pattern eventBus
 *
 * @param <Invoker> type for indication event-method
 */
public abstract class Event<Invoker>
{
    protected final Logger LOGGER;

    protected volatile Invoker invoker;

    protected Event() {
        LOGGER = LoggerFactory.getLogger(getClass());
    }

    public final Invoker invoker() {
        return invoker;
    }

    public abstract boolean register(Invoker invoker);
    public abstract boolean register(String name, Invoker invoker);

    public abstract boolean unregister(Invoker invoker);

    @SuppressWarnings(Warnings.unusedReturnValue)
    public abstract boolean unregister(String name);
}
