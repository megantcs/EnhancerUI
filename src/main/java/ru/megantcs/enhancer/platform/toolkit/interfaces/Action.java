package ru.megantcs.enhancer.platform.toolkit.interfaces;

@FunctionalInterface
public interface Action<T>
{
    void invoke(T args);
}
