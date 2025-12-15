package ru.megantcs.enhancer.platform.toolkit.events.api;

// supported lambda handler
public interface LambdaEvent<T>
{
    boolean contains(String name);
    boolean register(String name, T handler);
    boolean unregister(String name);
}
