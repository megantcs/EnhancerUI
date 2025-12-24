package ru.megantcs.enhancer.platform.toolkit.exceptions.container.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionContainer;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionItem;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentExceptionContainer extends DefaultExceptionContainer
{
    public ConcurrentExceptionContainer()
    {
        super(new CopyOnWriteArrayList<>());
    }
}
