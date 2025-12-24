package ru.megantcs.enhancer.hook.handlers;

import ru.megantcs.enhancer.hook.HotBarRenderHook;
import ru.megantcs.enhancer.hook.ScoreboardRenderHook;

public class HotBarPosHandler
{
    public static float x;
    public static float y;
    public static float height;
    public static float width;

    public static float xHotbar;
    public static float yHotbar;
    public static float heightHotbar;
    public static float widthHotbar;

    public static void init() {
        HotBarRenderHook.RENDER_BACKGROUND.register((e)->{
            x = e.left();
            y = e.top();
            height = e.bottom();
            width = e.right();
            return false;
        });

        HotBarRenderHook.RENDER_SELECT_SLOT.register((e)->{
            xHotbar = e.x();
            yHotbar = e.y();
            heightHotbar = e.height();
            widthHotbar = e.width();
            return false;
        });
    }
}
