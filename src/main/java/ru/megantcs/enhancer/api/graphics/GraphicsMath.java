package ru.megantcs.enhancer.api.graphics;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class GraphicsMath
{
    public static boolean isHovered(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseX - width <= x && mouseY >= y && mouseY - height <= y;
    }
    public static char getCharFromKeyCode(int keyCode, int scanCode, int modifiers) {
        InputUtil.Key key = InputUtil.fromKeyCode(keyCode, scanCode);

        String keyName = key.getTranslationKey();

        boolean capsLock = GLFW.glfwGetKey(
                MinecraftClient.getInstance().getWindow().getHandle(),
                GLFW.GLFW_KEY_CAPS_LOCK
        ) == GLFW.GLFW_PRESS;

        boolean shiftPressed = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;

        if (keyCode >= GLFW.GLFW_KEY_A && keyCode <= GLFW.GLFW_KEY_Z) {
            char base = (char) ('a' + (keyCode - GLFW.GLFW_KEY_A));

            if (capsLock) {
                return shiftPressed ? Character.toLowerCase(base)
                        : Character.toUpperCase(base);
            } else {
                return shiftPressed ? Character.toUpperCase(base)
                        : Character.toLowerCase(base);
            }
        }

        return getFallbackChar(keyCode, modifiers);
    }

    private static boolean isCapsLockActive() {
        long window = MinecraftClient.getInstance().getWindow().getHandle();
        return GLFW.glfwGetKey(window, GLFW.GLFW_KEY_CAPS_LOCK) == GLFW.GLFW_PRESS;
    }

    private static char getFallbackChar(int keyCode, int modifiers) {
        boolean shiftPressed = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;
        boolean capsLock = isCapsLockActive();

        switch (keyCode) {
            case GLFW.GLFW_KEY_SPACE -> {
                return ' ';
            }
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                return '\n';
            }
            case GLFW.GLFW_KEY_TAB -> {
                return '\t';
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                return '\b';
            }
            case GLFW.GLFW_KEY_MINUS -> {
                return shiftPressed ? '_' : '-';
            }
            case GLFW.GLFW_KEY_EQUAL -> {
                return shiftPressed ? '+' : '=';
            }
            case GLFW.GLFW_KEY_LEFT_BRACKET -> {
                return shiftPressed ? '{' : '[';
            }
            case GLFW.GLFW_KEY_RIGHT_BRACKET -> {
                return shiftPressed ? '}' : ']';
            }
            case GLFW.GLFW_KEY_BACKSLASH -> {
                return shiftPressed ? '|' : '\\';
            }
            case GLFW.GLFW_KEY_SEMICOLON -> {
                return shiftPressed ? ':' : ';';
            }
            case GLFW.GLFW_KEY_APOSTROPHE -> {
                return shiftPressed ? '"' : '\'';
            }
            case GLFW.GLFW_KEY_COMMA -> {
                return shiftPressed ? '<' : ',';
            }
            case GLFW.GLFW_KEY_PERIOD -> {
                return shiftPressed ? '>' : '.';
            }
            case GLFW.GLFW_KEY_SLASH -> {
                return shiftPressed ? '?' : '/';
            }
            case GLFW.GLFW_KEY_GRAVE_ACCENT -> {
                return shiftPressed ? '~' : '`';
            }
            case GLFW.GLFW_KEY_KP_DIVIDE -> {
                return '/';
            }
            case GLFW.GLFW_KEY_KP_MULTIPLY -> {
                return '*';
            }
            case GLFW.GLFW_KEY_KP_SUBTRACT -> {
                return '-';
            }
            case GLFW.GLFW_KEY_KP_ADD -> {
                return '+';
            }
            case GLFW.GLFW_KEY_KP_DECIMAL -> {
                return '.';
            }
            case GLFW.GLFW_KEY_KP_0, GLFW.GLFW_KEY_KP_1, GLFW.GLFW_KEY_KP_2,
                 GLFW.GLFW_KEY_KP_3, GLFW.GLFW_KEY_KP_4, GLFW.GLFW_KEY_KP_5,
                 GLFW.GLFW_KEY_KP_6, GLFW.GLFW_KEY_KP_7, GLFW.GLFW_KEY_KP_8,
                 GLFW.GLFW_KEY_KP_9 -> {
                return (char) ('0' + (keyCode - GLFW.GLFW_KEY_KP_0));
            }
            default -> {
                if (keyCode >= GLFW.GLFW_KEY_0 && keyCode <= GLFW.GLFW_KEY_9) {
                    char digit = (char) ('0' + (keyCode - GLFW.GLFW_KEY_0));
                    if (shiftPressed) {
                        switch (digit) {
                            case '1' -> {
                                return '!';
                            }
                            case '2' -> {
                                return '@';
                            }
                            case '3' -> {
                                return '#';
                            }
                            case '4' -> {
                                return '$';
                            }
                            case '5' -> {
                                return '%';
                            }
                            case '6' -> {
                                return '^';
                            }
                            case '7' -> {
                                return '&';
                            }
                            case '8' -> {
                                return '*';
                            }
                            case '9' -> {
                                return '(';
                            }
                            case '0' -> {
                                return ')';
                            }
                        }
                    }
                    return digit;
                }
                return 0;
            }
        }
    }
}
