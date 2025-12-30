package ru.megantcs.enhancer.platform.toolkit.debugs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.toolkit.api.AllStatic;

import java.util.Objects;

@AllStatic
public class Debug
{
    private static final Logger LOGGER = LoggerFactory.getLogger("Enhancer-Debug");

    public static void info(String format, Object... objects) {
        Objects.requireNonNull(format);
        Objects.requireNonNull(objects);

        LOGGER.info(format, objects);
    }

    public static void err(String format, Object... objects) {
        Objects.requireNonNull(format);
        Objects.requireNonNull(objects);

        LOGGER.error(format, objects);
    }


    public static void warn(String format, Object... objects) {
        Objects.requireNonNull(format);
        Objects.requireNonNull(objects);

        LOGGER.warn(format, objects);
    }
}
