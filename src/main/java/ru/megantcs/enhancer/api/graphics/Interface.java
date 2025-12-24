package ru.megantcs.enhancer.api.graphics;

import imgui.ImGuiIO;

public interface Interface
{
    default void preRender(ImGuiIO io) {}
    default void render(ImGuiIO io) {}
    default void postRender(ImGuiIO io) {}
}
