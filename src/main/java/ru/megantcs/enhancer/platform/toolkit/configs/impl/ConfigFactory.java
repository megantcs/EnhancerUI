package ru.megantcs.enhancer.platform.toolkit.configs.impl;

import ru.megantcs.enhancer.platform.toolkit.configs.api.Config;
import ru.megantcs.enhancer.platform.toolkit.configs.api.IConfigFactory;

public class ConfigFactory
{
    public static Config getConfig(String filename) {
        var IConfigFactory = getIConfigFactory();
        return IConfigFactory.getConfig(filename);
    }

    public static Config getConfig(String filename, String directory) {
        var IConfigFactory = getIConfigFactory();
        return IConfigFactory.getConfig(directory, filename);
    }

    private static IConfigFactory getIConfigFactory() {
        return new ConcreteClientConfigFactory();
    }
}
