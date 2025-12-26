package ru.megantcs.enhancer.platform.toolkit.reflect.FinishedObjects;

import ru.megantcs.enhancer.platform.toolkit.reflect.FinishedObjects.deleters.SetNullDeleter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Deprecated
@Retention(RetentionPolicy.RUNTIME)
public @interface FinishField
{
    Class<? extends Deleter> deleter() default SetNullDeleter.class;
}
