package ru.megantcs.enhancer.platform.toolkit.placeholders;

import java.util.HashMap;
import java.util.Map;

public class HashMapBuilder<TKey, TValue> extends MapBuilder.BaseMapBuilder<TKey, TValue> {
    public HashMapBuilder() {
        super(new HashMap<>());
    }
}
