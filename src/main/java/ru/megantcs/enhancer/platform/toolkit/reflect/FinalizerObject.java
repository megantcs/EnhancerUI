package ru.megantcs.enhancer.platform.toolkit.reflect;

import ru.megantcs.enhancer.platform.toolkit.api.API;
import ru.megantcs.enhancer.platform.toolkit.api.NotAdvised;

import java.lang.reflect.Field;
import java.util.Objects;

@NotAdvised("not a safe or stable version. but it is compatible")
@API(status = API.Status.MAINTAINED)
public class FinalizerObject
{
    public static void finalize(Object entity) throws IllegalAccessException {
        Objects.requireNonNull(entity);
        var fields = AnnotationSearcher.getFields(entity.getClass(), FinalizeField.class);
        for (Field field : fields) {
            if(!field.isAccessible()) {
                field.setAccessible(true);
            }

            field.set(entity, null);
        }
    }
}
