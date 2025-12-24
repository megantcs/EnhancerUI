package ru.megantcs.enhancer.platform.toolkit.exceptions.container.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionItem;

import java.util.Objects;

public class DefaultExceptionItem implements ExceptionItem {
    private final Logger logger;

    private final Throwable exception;
    private final String namespace;

    public DefaultExceptionItem(Throwable exception, String namespace) {
        this.exception = Objects.requireNonNull(exception);
        this.namespace = Objects.requireNonNull(namespace);
        this.logger    = LoggerFactory.getLogger(namespace);
    }

    @Override
    public String namespace() {
        return namespace;
    }

    @Override
    public Throwable exception() {
        return exception;
    }

    @Override
    public void log() {
        logger.error("Exception occurred", exception);
    }
}
