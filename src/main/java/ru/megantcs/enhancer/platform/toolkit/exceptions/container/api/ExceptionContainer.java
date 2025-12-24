package ru.megantcs.enhancer.platform.toolkit.exceptions.container.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.megantcs.enhancer.platform.toolkit.api.Noexcept;

import java.util.List;

public interface ExceptionContainer
{
    boolean add(@NotNull String namespace,
                @NotNull Throwable exception);

    default boolean add(@NotNull Class<?> clazz, @NotNull Throwable exception) {
        return this.add(clazz.getName(), exception);
    }

    @Nullable @Noexcept
    ExceptionItem last();

    @NotNull
    List<ExceptionItem> getByNamespace(String namespace);

    @NotNull
    List<ExceptionItem> getAll();

    void logLast();

    void logAll();


}
