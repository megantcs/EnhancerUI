package ru.megantcs.enhancer.platform.toolkit.events.eventbus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventBus
{
    Type type();

    enum Type {
        POST, PRE
    }
}
