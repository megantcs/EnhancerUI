package ru.megantcs.enhancer.platform.toolkit.events.eventbus.impl;

import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBus;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionContainer;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionFactory;

public class DefaultEventBus
        extends EventBus.BaseEventBus
{
    public DefaultEventBus() {
        super(ExceptionFactory.createContainer());
    }
}
