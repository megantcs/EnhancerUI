package ru.megantcs.Core.Platform;

public interface ConfigItem
{
    String serialize();
    void deserialize(String json);

    default void saveToFile(String configName) {
        Config.INSTANCE.saveToFile(this, configName);
    }

    default void loadToFile(String configName) {
        Config.INSTANCE.loadFromFile(this, configName);
    }
}
