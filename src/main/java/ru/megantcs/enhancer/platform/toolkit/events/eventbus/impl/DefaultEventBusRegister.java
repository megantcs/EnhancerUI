package ru.megantcs.enhancer.platform.toolkit.events.eventbus.impl;

import imgui.ImGuiIO;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import ru.megantcs.enhancer.api.graphics.ImGuiLoader;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBus;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventBusRegister;
import ru.megantcs.enhancer.platform.toolkit.events.eventbus.api.EventHandler;
import ru.megantcs.enhancer.platform.toolkit.interfaces.Action;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultEventBusRegister implements EventBusRegister {
    private final List<EventHandler> events;
    private final Map<EventHandler, Set<String>> executedFirstMethods;

    public DefaultEventBusRegister() {
        events = new CopyOnWriteArrayList<>();
        executedFirstMethods = new ConcurrentHashMap<>();
        init();
    }

    private void init() {
        ClientTickEvents.END_CLIENT_TICK.register(this::executeGameTick);
        HudRenderCallback.EVENT.register(this::executeHudRender);
        WorldRenderEvents.AFTER_ENTITIES.register(this::executeWorldRender);
        ImGuiLoader.INSTANCE.RENDER_FRAME_EVENT.register(this::executeImGuiRender);
    }

    private void executeGameTick(MinecraftClient mc) {
        executeMethod("onGameTick", handler -> handler.onGameTick(mc), MinecraftClient.class);
    }

    private void executeHudRender(DrawContext drawContext, float tickDelta) {
        executeMethod("onHudRender", handler -> handler.onHudRender(drawContext, tickDelta),
                DrawContext.class, float.class);
    }

    private void executeWorldRender(WorldRenderContext context) {
        executeMethod("onWorldRender", handler -> handler.onWorldRender(context),
                WorldRenderEvents.AfterEntities.class);
    }

    private void executeImGuiRender(ImGuiIO io) {
        executeMethod("imguiRender", handler -> handler.imguiRender(io), ImGuiIO.class);
    }

    private void executeMethod(String methodName, Action<EventHandler> action, Class<?>... paramTypes) {
        for (EventHandler event : events) {
            try {
                Method method = event.getClass().getMethod(methodName, paramTypes);
                EventBus annotation = method.getAnnotation(EventBus.class);

                if (annotation != null && annotation.type() == EventBus.Type.FIRST) {
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

    public void register(EventHandler eventHandler) {
        Objects.requireNonNull(eventHandler);
        events.add(eventHandler);
    }

    public boolean unregister(EventHandler eventHandler) {
        Objects.requireNonNull(eventHandler);
        executedFirstMethods.remove(eventHandler);
        return events.remove(eventHandler);
    }

    public void resetFirstExecution(EventHandler eventHandler) {
        Objects.requireNonNull(eventHandler);
        executedFirstMethods.remove(eventHandler);
    }

    public void resetAllFirstExecutions() {
        executedFirstMethods.clear();
    }
}