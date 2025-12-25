package ru.megantcs.enhancer.platform.toolkit.events.eventbus.api;

import imgui.ImGuiIO;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

// supported interface
public interface EventHandler
{
    default void onGameTick(MinecraftClient minecraftClient) {}
    default void onHudRender(DrawContext drawContext, float delta) {}
    default void onWorldRender(WorldRenderContext context) {}
    default void imguiRender(ImGuiIO io) {}
}
