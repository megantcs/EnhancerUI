package ru.megantcs.enhancer.platform.toolkit.exceptions.container.api;

import ru.megantcs.enhancer.platform.toolkit.exceptions.container.impl.DefaultExceptionItem;

public interface ExceptionItem
{
    String namespace();
    Throwable exception();

    void log();

    static ExceptionItem of(String namespace, Throwable throwable) {
        return new DefaultExceptionItem(throwable, namespace);
    }
}
