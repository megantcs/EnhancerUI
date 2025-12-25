package ru.megantcs.enhancer.platform.toolkit.configs.impl;

import ru.megantcs.enhancer.platform.toolkit.configs.api.Config;
import ru.megantcs.enhancer.platform.toolkit.configs.api.IConfigFactory;

import java.util.Objects;

public class ConcreteClientConfigFactory implements IConfigFactory
{
    @Override
    public Config getConfig(String filename) {
        Objects.requireNonNull(filename);
        return new ConcreteClientConfig(filename, "platform");
    }

    @Override
    public Config getConfig(String directory, String filename) {
        Objects.requireNonNull(directory);
        Objects.requireNonNull(filename);
        return new ConcreteClientConfig(filename, directory );
    }
}
