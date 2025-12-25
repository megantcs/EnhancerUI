package ru.megantcs.enhancer.platform.toolkit.configs;

public interface SerializeConfig
{
    String serialize();
    void deserialize(String json);

    default void saveToFile(String configName) {
        ConfigOLD.INSTANCE.saveToFile(this, configName);
    }

    default void loadToFile(String configName) {
        ConfigOLD.INSTANCE.loadFromFile(this, configName);
    }
}
