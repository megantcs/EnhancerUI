package ru.megantcs.enhancer.platform.toolkit.events.eventbus.fabric;

import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBusRegister;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.IEventBusFactory;

public class FabricEventBusFactory implements IEventBusFactory {

    private static final FabricEventBusRegister eventBusRegister;

    @Override
    public EventBusRegister getBusRegister() {
        return eventBusRegister;
    }

    static {
        eventBusRegister = new FabricEventBusRegister();
    }
}
