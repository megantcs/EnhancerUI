package ru.megantcs.enhancer;

import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.api.core.PartPipeline;
import ru.megantcs.enhancer.api.provider.PlayerInfoPlaceholderProvider;
import ru.megantcs.enhancer.api.lua.LuaSandBox;
import ru.megantcs.enhancer.api.lua.wrappers.*;
import ru.megantcs.enhancer.api.provider.SystemPlaceholderProvider;
import ru.megantcs.enhancer.impl.core.LuaWrappers.RenderObjectSingleton;
import ru.megantcs.enhancer.impl.core.LuaWrappers.RenderObjectWrapper;
import ru.megantcs.enhancer.loader.MixinLoader.CrosshairRenderMixin;
import ru.megantcs.enhancer.loader.MixinLoader.HotbarRenderMixin;
import ru.megantcs.enhancer.loader.MixinLoader.LuaMixinHub;
import ru.megantcs.enhancer.loader.MixinLoader.ScoreboardRenderMixin;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.EventBusFactory;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBus;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBusRegister;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.impl.DefaultEventBus;
import ru.megantcs.enhancer.platform.toolkit.placeholders.api.Placeholder;
import ru.megantcs.enhancer.platform.toolkit.placeholders.api.PlaceholderFactory;
import ru.megantcs.enhancer.platform.toolkit.api.API;
import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionFactory;

import java.lang.reflect.InvocationTargetException;

public class EnhancerPlatform
{
    private static final EventBusRegister PLATFORM_BUS_REGISTER;
    private static final EventBus PLATFORM_EVENT_BUS;
    private static final Placeholder BASE_PLACEHOLDER;
    private static final Placeholder PLATFORM_PLACEHOLDER;

    @API(status = API.Status.MAINTAINED)
    public static @NotNull LuaSandBox supportEnhancerSandbox() {
        LuaSandBox sandBox = emptySandBox();
        try {
            sandBox.loadClass(DebugWrapper.class);
            sandBox.loadClass(RenderObjectSingleton.class);
            sandBox.loadClass(RenderObjectWrapper.class);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return sandBox;
    }

    @API(status = API.Status.MAINTAINED)
    public static @NotNull LuaSandBox emptySandBox() {
        return new LuaSandBox(ExceptionFactory.createConcurrentContainer());
    }

    @API(status = API.Status.MAINTAINED)
    public static EventBusRegister platformFabricEventBus() {
        return PLATFORM_BUS_REGISTER;
    }

    public static EventBus platformEventBus() {
        return PLATFORM_EVENT_BUS;
    }

    @API(status = API.Status.MAINTAINED)
    public static Placeholder platformPlaceholder() {
        return PLATFORM_PLACEHOLDER;
    }

    @API(status = API.Status.MAINTAINED)
    public static Placeholder basePlaceholder() {
        return BASE_PLACEHOLDER;
    }

    static {
        PLATFORM_BUS_REGISTER = EventBusFactory.getInstanceFabricEvent();
        BASE_PLACEHOLDER = PlaceholderFactory.create("{", "}");
        PLATFORM_EVENT_BUS = EventBusFactory.getEventBus();
        PLATFORM_PLACEHOLDER = new PartPipeline<Placeholder>(BASE_PLACEHOLDER)
                .part(new SystemPlaceholderProvider(BASE_PLACEHOLDER))
                .part(new PlayerInfoPlaceholderProvider(PLATFORM_BUS_REGISTER, BASE_PLACEHOLDER)).get();
    }

    public static LuaMixinHub platformMixinHub()
    {
        var hub = new LuaMixinHub(supportEnhancerSandbox());
        hub.addModule(new HotbarRenderMixin());
        hub.addModule(new ScoreboardRenderMixin());
        hub.addModule(new CrosshairRenderMixin());

        return hub;
    }
}


