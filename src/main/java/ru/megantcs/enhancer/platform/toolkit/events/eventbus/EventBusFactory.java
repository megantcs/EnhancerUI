package ru.megantcs.enhancer.platform.toolkit.events.eventbus;

import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBusRegister;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.IEventBusFactory;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.impl.DefaultEventBusFactory;

public class EventBusFactory
{
    public static EventBusRegister getInstance() {
        var IEventBusFactory = getIEventBusFactory();
        return IEventBusFactory.getBusRegister();
    }

    private static IEventBusFactory getIEventBusFactory() {
        return new DefaultEventBusFactory();
    }
}
