package ru.megantcs.enhancer.platform.toolkit.events.eventbus.api;

@FunctionalInterface
public interface IEventBusFactory
{
    EventBusRegister getBusRegister();
}
