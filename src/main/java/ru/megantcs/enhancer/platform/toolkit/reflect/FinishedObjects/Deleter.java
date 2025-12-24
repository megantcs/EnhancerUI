package ru.megantcs.enhancer.platform.toolkit.reflect.FinishedObjects;

import java.lang.reflect.Field;

public interface Deleter
{
    void delete(Field field, Object sender) throws IllegalAccessException;
}
