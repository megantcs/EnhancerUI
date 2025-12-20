package ru.megantcs.enhancer.platform.toolkit.exceptions;

import ru.megantcs.enhancer.platform.toolkit.reflect.Noexcept;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExceptionContainer
{
    private final List<ExceptionItem> items;

    public ExceptionContainer() {
        items = new CopyOnWriteArrayList<>();
    }

    public boolean add(String namespace, Exception exception)
    {
        Objects.requireNonNull(namespace);
        Objects.requireNonNull(exception);

        return items.add(new ExceptionItem(namespace, exception));
    }

    @Noexcept
    public Optional<ExceptionItem> last() {
        if(items.isEmpty()) {
            return Optional.empty();
        }

        var item = items.get(0);
        if(item == null) return Optional.empty();

        return Optional.of(item);
    }

    public void printAll() {
        items.forEach(ExceptionItem::printException);
    }

    public void printLast() {
        if(items.isEmpty()) return;

        items.get(items.size() - 1).printException();
    }

    public boolean add(Class<?> clazz, Exception exception) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(exception);

        return items.add(new ExceptionItem(clazz.getName(), exception));
    }

    public Set<ExceptionItem> get(String namespace) {
        var list = new HashSet<ExceptionItem>();
        for (ExceptionItem item : items) {
            if(Objects.equals(item.namespace(), namespace)) {
                list.add(item);
            }
        }

        return list;
    }

    public Set<Exception> getExceptions() {
        var exceptions = new HashSet<Exception>();
        for (ExceptionItem item : items) {
            exceptions.add(item.exception());
        }
        return exceptions;
    }

    @Override
    public String toString() {
        return "ExceptionContainer{" +
                "items=" + items +
                '}';
    }
}
