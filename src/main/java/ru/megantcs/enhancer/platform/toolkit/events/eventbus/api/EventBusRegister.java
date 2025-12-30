package ru.megantcs.enhancer.platform.toolkit.events.eventbus.api;

import ru.megantcs.enhancer.platform.toolkit.api.API;
import ru.megantcs.enhancer.platform.toolkit.api.AccessExceptions;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.fabric.FabricEventHandler;

@API(status = API.Status.MAINTAINED)
public interface EventBusRegister
{
    @AccessExceptions(access = NullPointerException.class)
    void register(FabricEventHandler eventHandler);

    @AccessExceptions(access = NullPointerException.class)
    boolean unregister(FabricEventHandler eventHandler);
}
