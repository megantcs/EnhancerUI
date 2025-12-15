package ru.megantcs.enhancer.platform.toolkit.interfaces;

@FunctionalInterface
public interface Func<TArgument, TReturn>
{
    TReturn run(TArgument argument);
}
