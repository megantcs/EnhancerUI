package ru.megantcs.enhancer.platform.toolkit.events.eventbus.impl;

import imgui.ImGuiIO;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import ru.megantcs.enhancer.api.graphics.ImGuiLoader;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventSubscribe;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBusRegister;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.FabricEventHandler;
import ru.megantcs.enhancer.platform.toolkit.interfaces.Action;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class FabricEventBusRegister implements EventBusRegister {
    private final List<FabricEventHandler> events;
    private final Map<FabricEventHandler, Set<String>> executedFirstMethods;

    public FabricEventBusRegister() {
        events = new CopyOnWriteArrayList<>();
        executedFirstMethods = new ConcurrentHashMap<>();
        init();
    }

    private void init() {
        ClientTickEvents.END_CLIENT_TICK.register(this::executeGameTick);
        HudRenderCallback.EVENT.register(this::executeHudRender);
        WorldRenderEvents.AFTER_ENTITIES.register(this::executeWorldRenderAfter);
        WorldRenderEvents.BEFORE_ENTITIES.register(this::executeWorldRenderBefore);

        ImGuiLoader.INSTANCE.RENDER_FRAME_EVENT.register(this::executeImGuiRender);
    }

    private void executeWorldRenderBefore(WorldRenderContext worldRenderContext) {
        executeMethod("onWorldRenderBefore", handler -> handler.onWorldRenderBefore(worldRenderContext),
                WorldRenderEvents.AfterEntities.class);
    }

    private void executeGameTick(MinecraftClient mc) {
        executeMethod("onGameTick", handler -> handler.onGameTick(mc), MinecraftClient.class);
    }

    private void executeHudRender(DrawContext drawContext, float tickDelta) {
        executeMethod("onHudRender", handler -> handler.onHudRender(drawContext, tickDelta),
                DrawContext.class, float.class);
    }

    private void executeWorldRenderAfter(WorldRenderContext context) {
        executeMethod("onWorldRenderAfter", handler -> handler.onWorldRenderAfter(context),
                WorldRenderEvents.AfterEntities.class);
    }

    private void executeImGuiRender(ImGuiIO io) {
        executeMethod("imguiRender", handler -> handler.imguiRender(io), ImGuiIO.class);
    }

    private void executeMethod(String methodName, Action<FabricEventHandler> action, Class<?>... paramTypes) {
        for (FabricEventHandler event : events) {
            try {
                Method method = event.getClass().getMethod(methodName, paramTypes);
                EventSubscribe annotation = method.getAnnotation(EventSubscribe.class);

                if (annotation != null && annotation.type() == EventSubscribe.Type.FIRST) {
                    Set<String> executedMethods = executedFirstMethods
                            .computeIfAbsent(event, k -> ConcurrentHashMap.newKeySet());

                    if (!executedMethods.contains(methodName)) {
                        action.invoke(event);
                        executedMethods.add(methodName);
                    }
                } else {
                    action.invoke(event);
                }
            } catch (NoSuchMethodException ignored) {}
              catch (Exception e) {
                e.printStackTrace();
              }
        }
    }

    public void register(FabricEventHandler eventHandler) {
        Objects.requireNonNull(eventHandler);
        events.add(eventHandler);
    }

    public boolean unregister(FabricEventHandler eventHandler) {
        Objects.requireNonNull(eventHandler);
        executedFirstMethods.remove(eventHandler);
        return events.remove(eventHandler);
    }

    public void resetFirstExecution(FabricEventHandler eventHandler) {
        Objects.requireNonNull(eventHandler);
        executedFirstMethods.remove(eventHandler);
    }

    public void resetAllFirstExecutions() {
        executedFirstMethods.clear();
    }
}