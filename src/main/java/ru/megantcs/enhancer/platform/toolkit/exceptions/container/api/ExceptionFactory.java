package ru.megantcs.enhancer.platform.toolkit.exceptions.container.api;

import ru.megantcs.enhancer.platform.toolkit.exceptions.container.impl.ConcurrentExceptionContainer;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.impl.DefaultExceptionContainer;

import java.util.ArrayList;
import java.util.function.Supplier;

public interface ExceptionFactory
{
    static ExceptionContainer createContainer() {
        return DefaultExceptionContainer.of(new ArrayList<>());
    }

    static ExceptionContainer createConcurrentContainer() {
        return new ConcurrentExceptionContainer();
    }
}
