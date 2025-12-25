package ru.megantcs.enhancer.platform.toolkit.events.eventbus.impl;

import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBusRegister;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.IEventBusFactory;

public class DefaultEventBusFactory implements IEventBusFactory {

    private static final DefaultEventBusRegister eventBusRegister;

    @Override
    public EventBusRegister getBusRegister() {
        return eventBusRegister;
    }

    static {
        eventBusRegister = new DefaultEventBusRegister();
    }
}
