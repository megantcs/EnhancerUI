package ru.megantcs.enhancer.platform.toolkit.reflect.FinishedObjects;

import java.lang.reflect.Field;

@Deprecated
public class FinishFieldImpl
{
    public static void finishObj(Object obj) throws IllegalAccessException, InstantiationException {
        var clazz = obj.getClass();

        var fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            FinishField ann = field.getAnnotation(FinishField.class);
            if(ann == null) continue;

            ann.deleter().newInstance().delete(field, obj);
        }
    }
}
