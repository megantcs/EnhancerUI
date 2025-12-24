package ru.megantcs.enhancer.hook.handlers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import ru.megantcs.enhancer.hook.BossBarRenderHook;
import ru.megantcs.enhancer.mixin.BossBarHudAccessor;

public class BossBarPosHandler
{
    public static float move_x;
    public static float move_y;
    public static float move_width;
    public static float move_height;

    public static float x;
    public static float y;
    public static float height;
    public static float width;

    public static boolean isActivity() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null || client.world == null || client.player == null) {
            return false;
        }

        BossBarHud bossBarHud = client.inGameHud.getBossBarHud();
        BossBarHudAccessor accessor = (BossBarHudAccessor) bossBarHud;

        var bossBars = accessor.getBossBars();
        return bossBars != null && !bossBars.isEmpty();
    }

    public static void init()
    {
        BossBarRenderHook.RENDER_BACKGROUND.register((e)->{
           x = e.x();
           y = e.y();
           width = e.width();
           height = e.height();

           return false;
        });
        BossBarRenderHook.RENDER_PROGRESS.register((e)->{
            move_x = e.x();
            move_y = e.y();
            move_width = e.width();
            move_height = e.height();
            return false;
        });
    }
}
