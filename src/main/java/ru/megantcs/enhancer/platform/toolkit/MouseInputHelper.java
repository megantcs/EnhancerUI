package ru.megantcs.ImHudEditor.platform.utils;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class MouseInputHelper {
    private static boolean wasRightMousePressed = false;

    public static boolean isRightMouseReleased() {
        MinecraftClient client = MinecraftClient.getInstance();
        long window = client.getWindow().getHandle();
        boolean isPressedNow = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;

        boolean wasReleased = wasRightMousePressed && !isPressedNow;
        wasRightMousePressed = isPressedNow;

        return wasReleased;
    }
}