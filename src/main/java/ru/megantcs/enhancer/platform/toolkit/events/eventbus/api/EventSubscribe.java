package ru.megantcs.enhancer.platform.toolkit.events.eventbus.api;

import ru.megantcs.enhancer.platform.toolkit.events.eventbus.fabric.FabricEventSubscribe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventSubscribe
{
    Type type() default Type.POST;

    enum Type
    {
        POST, PRE
    }
}
