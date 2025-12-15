package ru.megantcs.enhancer.platform.toolkit.events.api;

public interface SupportedEvent<T> extends LambdaEvent<T>
{
    boolean contains(T t);
}
