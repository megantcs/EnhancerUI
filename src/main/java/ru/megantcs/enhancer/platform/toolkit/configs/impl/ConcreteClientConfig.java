package ru.megantcs.enhancer.platform.toolkit.configs.impl;
import net.minecraft.client.MinecraftClient;
import ru.megantcs.enhancer.platform.toolkit.configs.api.Config;

import java.nio.file.Paths;

public class ConcreteClientConfig extends Config.BaseConfigImpl
{
    public ConcreteClientConfig(String fileName, String directory) {
        super(fileName, Paths.get(MinecraftClient.getInstance().runDirectory.getPath(), "config", directory).toString());
    }
}
