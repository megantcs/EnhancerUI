package ru.megantcs.enhancer.platform.toolkit.reflect.FinishedObjects.deleters;

import ru.megantcs.enhancer.platform.toolkit.reflect.FinishedObjects.Deleter;

import java.lang.reflect.Field;

public class SetNullDeleter implements Deleter
{

    @Override
    public void delete(Field field, Object sender) throws IllegalAccessException {
        field.set(sender, null);
    }
}
