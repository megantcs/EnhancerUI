package ru.megantcs.enhancer.hook.handlers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import ru.megantcs.enhancer.hook.*;

import java.util.HashMap;
import java.util.Map;

public class ScreenHookHandler {
    private static final Map<Class<? extends Screen>, ScreenOverlay> screenOverlays = new HashMap<>();
    private static boolean isProcessing = false;

    public static void hookRender(Screen screen, Class<? extends Screen> toScreen) {
        screenOverlays.put(toScreen, new ScreenOverlay(screen));
    }

    public static void init() {
        ScreenRenderHook.SCREEN_RENDER.register((e) -> {
            if (MinecraftClient.getInstance().currentScreen == null || isProcessing) {
                return false;
            }

            Class<? extends Screen> currentScreenClass = MinecraftClient.getInstance().currentScreen.getClass();
            ScreenOverlay overlay = screenOverlays.get(currentScreenClass);

            if (overlay != null) {
                try {
                    isProcessing = true;
                    overlay.render(e.drawContext(), e.mouseX(), e.mouseY(), e.delta());
                } finally {
                    isProcessing = false;
                }
            }

            return false;
        });

        ScreenRenderHook.SCREEN_CLOSE.register((e) -> {
            if (MinecraftClient.getInstance().currentScreen == null || isProcessing) {
                return false;
            }

            Class<? extends Screen> currentScreenClass = MinecraftClient.getInstance().currentScreen.getClass();
            ScreenOverlay overlay = screenOverlays.get(currentScreenClass);

            if (overlay != null) {
                try {
                    isProcessing = true;
                    overlay.close();
                } finally {
                    isProcessing = false;
                }
            }

            return false;
        });

        ScreenRenderHook.SCREEN_CHAR_TYPED.register((e) -> {
            if (MinecraftClient.getInstance().currentScreen == null || isProcessing) {
                return false;
            }

            Class<? extends Screen> currentScreenClass = MinecraftClient.getInstance().currentScreen.getClass();
            ScreenOverlay overlay = screenOverlays.get(currentScreenClass);

            if (overlay != null) {
                try {
                    isProcessing = true;
                    return overlay.charTyped((char) e.keyCode(), e.modifiers());
                } finally {
                    isProcessing = false;
                }
            }

            return false;
        });

        ScreenRenderHook.SCREEN_MOUSE_SCROLL.register((e) -> {
            if (MinecraftClient.getInstance().currentScreen == null || isProcessing) {
                return false;
            }

            Class<? extends Screen> currentScreenClass = MinecraftClient.getInstance().currentScreen.getClass();
            ScreenOverlay overlay = screenOverlays.get(currentScreenClass);

            if (overlay != null) {
                try {
                    isProcessing = true;
                    return overlay.mouseScrolled(e.mouseX(), e.mouseY(), e.amount());
                } finally {
                    isProcessing = false;
                }
            }

            return false;
        });

        ScreenRenderHook.SCREEN_MOUSE_CLICKED.register((e) -> {
            if (MinecraftClient.getInstance().currentScreen == null || isProcessing) {
                return false;
            }

            Class<? extends Screen> currentScreenClass = MinecraftClient.getInstance().currentScreen.getClass();
            ScreenOverlay overlay = screenOverlays.get(currentScreenClass);

            if (overlay != null) {
                try {
                    isProcessing = true;
                    return overlay.mouseClicked(e.mouseX(), e.mouseY(), e.button());
                } finally {
                    isProcessing = false;
                }
            }

            return false;
        });

        ScreenRenderHook.SCREEN_MOUSE_DRAGGED.register((e) -> {
            if (MinecraftClient.getInstance().currentScreen == null || isProcessing) {
                return false;
            }

            Class<? extends Screen> currentScreenClass = MinecraftClient.getInstance().currentScreen.getClass();
            ScreenOverlay overlay = screenOverlays.get(currentScreenClass);

            if (overlay != null) {
                try {
                    isProcessing = true;
                    return overlay.mouseDragged(e.mouseX(), e.mouseY(), e.button(), e.deltaX(), e.deltaY());
                } finally {
                    isProcessing = false;
                }
            }

            return false;
        });

        ScreenRenderHook.SCREEN_KEY_PRESSED.register((e) -> {
            if (MinecraftClient.getInstance().currentScreen == null || isProcessing) {
                return false;
            }

            Class<? extends Screen> currentScreenClass = MinecraftClient.getInstance().currentScreen.getClass();
            ScreenOverlay overlay = screenOverlays.get(currentScreenClass);

            if (overlay != null) {
                try {
                    isProcessing = true;
                    return overlay.keyPressed(e.keyCode(), e.scanCode(), e.modifiers());
                } finally {
                    isProcessing = false;
                }
            }

            return false;
        });

        ScreenRenderHook.SCREEN_KEY_RELEASED.register((e) -> {
            if (MinecraftClient.getInstance().currentScreen == null || isProcessing) {
                return false;
            }

            Class<? extends Screen> currentScreenClass = MinecraftClient.getInstance().currentScreen.getClass();
            ScreenOverlay overlay = screenOverlays.get(currentScreenClass);

            if (overlay != null) {
                try {
                    isProcessing = true;
                    return overlay.keyReleased(e.keyCode(), e.scanCode(), e.modifiers());
                } finally {
                    isProcessing = false;
                }
            }

            return false;
        });
    }

    private static class ScreenOverlay {
        private final Screen screen;
        private boolean isInitialized = false;

        public ScreenOverlay(Screen screen) {
            this.screen = screen;
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            if (!isInitialized) {
                screen.init(MinecraftClient.getInstance(), context.getScaledWindowWidth(), context.getScaledWindowHeight());
                isInitialized = true;
            }

            screen.resize(MinecraftClient.getInstance(), context.getScaledWindowWidth(), context.getScaledWindowHeight());
            screen.render(context, mouseX, mouseY, delta);
        }

        public void close() {
            screen.close();
            isInitialized = false;
        }

        public boolean charTyped(char chr, int modifiers) {
            return screen.charTyped(chr, modifiers);
        }

        public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
            return screen.mouseScrolled(mouseX, mouseY, amount);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return screen.mouseClicked(mouseX, mouseY, button);
        }

        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            return screen.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return screen.keyPressed(keyCode, scanCode, modifiers);
        }

        public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
            return screen.keyReleased(keyCode, scanCode, modifiers);
        }
    }

    public static void removeHook(Class<? extends Screen> screenClass) {
        screenOverlays.remove(screenClass);
    }

    public static void clearAllHooks() {
        screenOverlays.clear();
    }

    public static boolean hasHook(Class<? extends Screen> screenClass) {
        return screenOverlays.containsKey(screenClass);
    }
}