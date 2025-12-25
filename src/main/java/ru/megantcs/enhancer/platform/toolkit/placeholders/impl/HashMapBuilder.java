package ru.megantcs.enhancer.platform.toolkit.placeholders.impl;

import ru.megantcs.enhancer.platform.toolkit.placeholders.api.MapBuilder;

import java.util.HashMap;

public class HashMapBuilder<TKey, TValue> extends MapBuilder.BaseMapBuilder<TKey, TValue> {
    public HashMapBuilder() {
        super(new HashMap<>());
    }
}
