package ru.megantcs.enhancer.hook.handlers;

public class HookHandlersManager
{
    public static void init() {
        ScoreboardPosHandler.init();
        HotBarPosHandler.init();
        BossBarPosHandler.init();
        ScreenHookHandler.init();
    }
}
