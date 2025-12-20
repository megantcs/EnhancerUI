package ru.megantcs.enhancer.platform.toolkit.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.toolkit.reflect.Noexcept;

import java.util.HashMap;
import java.util.Map;

public record ExceptionItem(String namespace, Exception exception)
{
    private static final Map<String, Logger> INSTANCES = new HashMap<>();

    @Noexcept
    public void printException() {
        Logger logger;
        if(!INSTANCES.containsKey(namespace)) {
            INSTANCES.put(namespace, LoggerFactory.getLogger(namespace));
        }
        logger = INSTANCES.get(namespace);

        logger.error("An exception has occurred", exception);
    }
}
