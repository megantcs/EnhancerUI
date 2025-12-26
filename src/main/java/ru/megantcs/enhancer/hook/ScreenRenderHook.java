package ru.megantcs.enhancer.hook;

import net.minecraft.client.gui.screen.Screen;
import ru.megantcs.enhancer.hook.data.*;
import ru.megantcs.enhancer.platform.toolkit.events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.events.impl.FuncEvent;

public interface ScreenRenderHook
{
    FuncEvent<ScreenRenderHookRenderData, Boolean> SCREEN_RENDER = EventFactory.makeFuncEvent(false);
    FuncEvent<ScreenRenderHookKeyPressedData, Boolean> SCREEN_KEY_PRESSED = EventFactory.makeFuncEvent(false);
    FuncEvent<ScreenRenderHookMouseClicked, Boolean> SCREEN_MOUSE_CLICKED = EventFactory.makeFuncEvent(false);
    FuncEvent<ScreenRenderHookMouseDragged, Boolean> SCREEN_MOUSE_DRAGGED = EventFactory.makeFuncEvent(false);
    FuncEvent<ScreenRenderHookMouseScroll, Boolean> SCREEN_MOUSE_SCROLL = EventFactory.makeFuncEvent(false);
    FuncEvent<ScreenRenderHookKeyReleased, Boolean> SCREEN_KEY_RELEASED = EventFactory.makeFuncEvent(false);
    FuncEvent<ScreenRenderHookCharTyped, Boolean> SCREEN_CHAR_TYPED = EventFactory.makeFuncEvent(false);
    FuncEvent<Screen, Boolean> SCREEN_CLOSE = EventFactory.makeFuncEvent(false);
}
