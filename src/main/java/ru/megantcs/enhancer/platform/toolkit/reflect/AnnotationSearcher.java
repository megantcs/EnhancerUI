package ru.megantcs.enhancer.platform.toolkit.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AnnotationSearcher
{
    public static Set<Field> getFields(Class<?> src, Class<? extends Annotation> annotation)
    {
        Objects.requireNonNull(src);
        Objects.requireNonNull(annotation);

        var result = new HashSet<Field>();
        var fields = src.getDeclaredFields();

        for (Field field : fields)
        {
            if(field.getAnnotation(annotation) != null)
            {
                result.add(field);
            }
        }
        return result;
    }

    public static Set<Method> getMethods(Class<?> src, Class<? extends Annotation> annotation)
    {
        Objects.requireNonNull(src);
        Objects.requireNonNull(annotation);

        var result = new HashSet<Method>();
        var fields = src.getDeclaredMethods();

        for (Method field : fields)
        {
            if(field.getAnnotation(annotation) != null)
            {
                result.add(field);
            }
        }
        return result;
    }
}
