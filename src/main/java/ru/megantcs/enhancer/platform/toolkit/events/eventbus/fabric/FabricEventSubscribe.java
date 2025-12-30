package ru.megantcs.enhancer.platform.toolkit.events.eventbus.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventSubscribe
{
    Type type() default Type.ALWAYS;

    enum Type {
        FIRST, ALWAYS
    }
}
