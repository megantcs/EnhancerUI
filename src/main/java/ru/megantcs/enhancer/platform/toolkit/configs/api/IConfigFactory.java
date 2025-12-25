package ru.megantcs.enhancer.platform.toolkit.configs.api;

public interface IConfigFactory
{
    Config getConfig(String filename);
    Config getConfig(String directory, String filename);
}
