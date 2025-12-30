package ru.megantcs.enhancer.platform.toolkit.events.eventbus;

import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBus;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBusRegister;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.IEventBusFactory;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.fabric.FabricEventBusFactory;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.impl.DefaultEventBus;

public class EventBusFactory
{
    public static EventBusRegister getInstanceFabricEvent() {
        var IEventBusFactory = getIEventBusFactory();
        return IEventBusFactory.getBusRegister();
    }

    public static EventBus getEventBus() {
        return new DefaultEventBus();
    }

    private static IEventBusFactory getIEventBusFactory() {
        return new FabricEventBusFactory();
    }
}
