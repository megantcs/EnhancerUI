package ru.megantcs.enhancer.hook.handlers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import ru.megantcs.enhancer.hook.ScoreboardRenderHook;

public class ScoreboardPosHandler {
    private static float minX = Float.MAX_VALUE;
    private static float maxX = Float.MIN_VALUE;
    private static float minY = Float.MAX_VALUE;
    private static float maxY = Float.MIN_VALUE;

    private static boolean hasData = false;
    private static float x = 0;
    private static float y = 0;
    private static float width = 0;
    private static float height = 0;

    public static boolean isActivity() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.world == null || client.player == null) {
            return false;
        }

        Scoreboard scoreboard = client.world.getScoreboard();
        ScoreboardObjective sidebarObjective = scoreboard.getObjectiveForSlot(1);

        return sidebarObjective != null &&
                !scoreboard.getAllPlayerScores(sidebarObjective).isEmpty();
    }

    public static void init() {
        ScoreboardRenderHook.RENDER_BACKGROUND.register(params -> {
            hasData = true;

            if (params.left() < minX) minX = params.left();
            if (params.right() > maxX) maxX = params.right();
            if (params.top() < minY) minY = params.top();
            if (params.bottom() > maxY) maxY = params.bottom();

            return false;
        });

        ScoreboardRenderHook.RENDER_END.register(renderInfo -> {
            if (hasData) {
                x = minX;
                y = minY;
                width = (maxX - minX);
                height = (maxY - minY);
            } else {
                x = y = width = height = 0;
            }

            resetBounds();
            return false;
        });
    }

    private static void resetBounds() {
        minX = Float.MAX_VALUE;
        maxX = Float.MIN_VALUE;
        minY = Float.MAX_VALUE;
        maxY = Float.MIN_VALUE;
        hasData = false;
    }

    public static float getX() {
        return x;
    }

    public static float getY() {
        return y;
    }

    public static float getHeight() {
        return height;
    }

    public static float getWidth() {
        return width;
    }

    public static boolean hasValidData() {
        return width > 0 && height > 0;
    }
}