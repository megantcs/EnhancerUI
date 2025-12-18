package ru.megantcs.enhancer.hook;

import ru.megantcs.enhancer.hook.handlers.HotBarPosHandler;
import ru.megantcs.enhancer.hook.handlers.ScoreboardPosHandler;

public class HookHandlersManager
{
    public static void init() {
        ScoreboardPosHandler.init();
        HotBarPosHandler.init();
    }
}
