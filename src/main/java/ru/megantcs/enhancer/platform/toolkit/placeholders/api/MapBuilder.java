package ru.megantcs.enhancer.platform.toolkit.placeholders.api;

import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.platform.toolkit.api.Noexcept;

import java.util.Map;
import java.util.Objects;

public interface MapBuilder<TKey, TValue>
{
    @Noexcept
    @NotNull MapBuilder<TKey, TValue> put(TKey key, TValue value);

    @Noexcept
    @NotNull Map<TKey, TValue> get();

    class BaseMapBuilder<TKey, TValue>
            implements MapBuilder<TKey, TValue>
    {
        private final Map<TKey, TValue> data;

        protected BaseMapBuilder(Map<TKey, TValue> mapType) {
            data = Objects.requireNonNull(mapType);
        }

        public boolean empty() {
            return data.isEmpty();
        }

        @Override
        public @NotNull MapBuilder<TKey, TValue> put(TKey key, TValue value) {
            data.put(key, value);
            return this;
        }

        @Override
        public @NotNull Map<TKey, TValue> get() {
            return data;
        }
    }
}
