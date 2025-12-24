package ru.megantcs.enhancer;

import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.api.MinecraftPlaceholderHandler;
import ru.megantcs.enhancer.api.lua.LuaSandBox;
import ru.megantcs.enhancer.impl.core.LuaWrappers.RenderObjectSingleton;
import ru.megantcs.enhancer.impl.core.LuaWrappers.RenderObjectWrapper;
import ru.megantcs.enhancer.platform.toolkit.Placeholders.Placeholder;
import ru.megantcs.enhancer.platform.toolkit.Placeholders.PlaceholderFactory;
import ru.megantcs.enhancer.platform.toolkit.api.API;
import ru.megantcs.enhancer.platform.toolkit.configs.Config;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.EventBusRegister;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class EnhancerPlatform
{
    private static final EventBusRegister PLATFORM_BUS_REGISTER;
    private static final MinecraftPlaceholderHandler PLATFORM_PLACEHOLDER;

    @API(status = API.Status.MAINTAINED)
    public static @NotNull LuaSandBox supportEnhancer() {
        LuaSandBox sandBox = empty();
        try {
            sandBox.loadClass(RenderObjectSingleton.class);
            sandBox.loadClass(RenderObjectWrapper.class);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return sandBox;
    }

    @API(status = API.Status.MAINTAINED)
    public static @NotNull LuaSandBox empty() {
        return new LuaSandBox(ExceptionFactory.createConcurrentContainer());
    }

    @API(status = API.Status.MAINTAINED)
    public static @NotNull Config createConfig(String modName) {
        return new Config(Objects.requireNonNull(modName));
    }

    @API(status = API.Status.MAINTAINED)
    public static @NotNull Config platformConfig() {
        return Config.INSTANCE;
    }

    @API(status = API.Status.MAINTAINED)
    public static EventBusRegister platformEventBus() {
        return PLATFORM_BUS_REGISTER;
    }

    @API(status = API.Status.MAINTAINED)
    public static Placeholder platformPlaceholder() {
        return PLATFORM_PLACEHOLDER.getPlaceholder();
    }

    static {
        PLATFORM_BUS_REGISTER = EventBusRegister.getInstance();
        PLATFORM_PLACEHOLDER = MinecraftPlaceholderHandler.create(platformEventBus(), PlaceholderFactory.create("{", "}"));
    }
}
