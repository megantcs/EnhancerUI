package ru.megantcs.enhancer.platform.toolkit.exceptions.container.impl;

import com.sun.jna.platform.win32.WinUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionContainer;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DefaultExceptionContainer implements ExceptionContainer
{
    private final List<ExceptionItem> items;

    protected DefaultExceptionContainer(List<ExceptionItem> listType) {
        this.items = Objects.requireNonNull(listType);
    }

    @Override
    public boolean add(@NotNull String namespace, @NotNull Throwable exception) {
        Objects.requireNonNull(namespace);
        Objects.requireNonNull(exception);

        return items.add(ExceptionItem.of(namespace, exception));
    }

    @Override
    public @Nullable ExceptionItem last() {
        if(items.isEmpty()) return null;
        return items.get(items.size() - 1);
    }

    @Override
    public @NotNull List<ExceptionItem> getByNamespace(String namespace) {
        var list = new ArrayList<ExceptionItem>();
        for (ExceptionItem item : items) {
            if(item.namespace().equals(namespace)) list.add(item);
        }
        return list;
    }

    @Override
    public @NotNull List<ExceptionItem> getAll() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public void logLast() {
        var last = last();
        if(last != null) last.log();
    }

    @Override
    public void logAll() {
        items.forEach(ExceptionItem::log);
    }

    public static DefaultExceptionContainer of(List<ExceptionItem> listType) {
        return new DefaultExceptionContainer(listType);
    }
}
