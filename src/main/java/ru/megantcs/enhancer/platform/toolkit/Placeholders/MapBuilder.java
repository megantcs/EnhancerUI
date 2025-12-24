package ru.megantcs.enhancer.platform.toolkit.Placeholders;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapBuilder<TKey, TValue>
{
    private final Map<TKey, TValue> data;

    public MapBuilder(Map<TKey, TValue> mapType) {
        data = Objects.requireNonNull(mapType);
    }

    public MapBuilder() {
        data = new HashMap<>();
    }

    public MapBuilder<TKey, TValue> put(TKey key, TValue value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        data.put(key, value);
        return this;
    }

    public Map<TKey, TValue> get() {
        return data;
    }
}
