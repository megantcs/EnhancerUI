package ru.megantcs.enhancer.platform.toolkit.events.eventbus.fabric;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FabricEventSubscribe
{
    Type type() default Type.ALWAYS;

    enum Type {
        FIRST, ALWAYS
    }
}
