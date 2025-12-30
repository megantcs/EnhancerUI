package ru.megantcs.enhancer.mixin;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.megantcs.enhancer.api.lua.utils.PosObject;
import ru.megantcs.enhancer.hook.CrosshairRenderHook;
import ru.megantcs.enhancer.hook.HotBarRenderHook;
import ru.megantcs.enhancer.hook.ScoreboardRenderHook;
import ru.megantcs.enhancer.hook.data.CrosshairRenderHookData;
import ru.megantcs.enhancer.hook.data.HotbarRenderHookData;
import ru.megantcs.enhancer.hook.data.ScoreboardRenderHookData;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.client.gui.widget.ClickableWidget.WIDGETS_TEXTURE;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin
{
    @Shadow
    private int scaledHeight;

    @Shadow
    private int scaledWidth;

    @Unique
    private MinecraftClient client = MinecraftClient.getInstance();

    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    protected abstract void renderHotbarItem(DrawContext context, int x, int y, float f, PlayerEntity player, ItemStack stack, int seed);

    @Shadow
    @Final
    private static Identifier ICONS;

    @Shadow
    protected abstract boolean shouldRenderSpectatorCrosshair(HitResult hitResult);

    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    private void renderCrosshair(DrawContext context, CallbackInfo callbackInfo)
    {
        var cancel = CrosshairRenderHook.CROSSHAIR_RENDER.emit(new CrosshairRenderHookData((float) (this.scaledWidth - 15) / 2, (float) (this.scaledHeight - 15) / 2, 15, 15));
        if(cancel) {
            callbackInfo.cancel();
            return;
        }
    }

    @Inject(at = @At("HEAD"), method = "renderHotbar", cancellable = true)
    public void renderHotbar(float tickDelta, DrawContext context, CallbackInfo ci)
    {
        ci.cancel();
        var client = MinecraftClient.getInstance();

        PlayerEntity playerEntity = getCameraPlayer();
        if (playerEntity != null) {
            ItemStack itemStack = playerEntity.getOffHandStack();
            Arm arm = playerEntity.getMainArm().getOpposite();
            int i = this.scaledWidth / 2;
            int j = 182;
            int k = 91;

            var cancel = HotBarRenderHook.RENDER_BACKGROUND.emit(new HotbarRenderHookData(
                    context,
                    i - 91,
                    this.scaledHeight - 22,
                    182,
                    22,
                    0
            ));
            if(!cancel) {
                context.getMatrices().push();
                context.getMatrices().translate(0.0F, 0.0F, -90.0F);
                context.drawTexture(WIDGETS_TEXTURE, i - 91, this.scaledHeight - 22, 0, 0, 182, 22);
            }
            var cancel2 = HotBarRenderHook.RENDER_SELECT_SLOT.emit(new PosObject(i - 91 - 1 + playerEntity.getInventory().selectedSlot * 20,
                    this.scaledHeight - 22 - 1, 24, 24));
            if(!cancel2) {
                context.drawTexture(WIDGETS_TEXTURE, i - 91 - 1 + playerEntity.getInventory().selectedSlot * 20, this.scaledHeight - 22 - 1, 0, 22, 24, 22);
            }
            if(!cancel) {
                if (!itemStack.isEmpty()) {
                    if (arm == Arm.LEFT) {
                        context.drawTexture(WIDGETS_TEXTURE, i - 91 - 29, this.scaledHeight - 23, 24, 22, 29, 24);
                    } else {
                        context.drawTexture(WIDGETS_TEXTURE, i + 91, this.scaledHeight - 23, 53, 22, 29, 24);
                    }
                }

                context.getMatrices().pop();
            }
            int l = 1;

            for(int m = 0; m < 9; ++m) {
                int n = i - 90 + m * 20 + 2;
                int o = this.scaledHeight - 16 - 3;
                renderHotbarItem(context, n, o, tickDelta, playerEntity, (ItemStack)playerEntity.getInventory().main.get(m), l++);
            }

            if (!itemStack.isEmpty()) {
                int m = this.scaledHeight - 16 - 3;
                if (arm == Arm.LEFT) {
                    renderHotbarItem(context, i - 91 - 26, m, tickDelta, playerEntity, itemStack, l++);
                } else {
                    renderHotbarItem(context, i + 91 + 10, m, tickDelta, playerEntity, itemStack, l++);
                }
            }

            RenderSystem.enableBlend();
            if (client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR) {
                float f = client.player.getAttackCooldownProgress(0.0F);
                if (f < 1.0F) {
                    int n = this.scaledHeight - 20;
                    int o = i + 91 + 6;
                    if (arm == Arm.RIGHT) {
                        o = i - 91 - 22;
                    }

                    int p = (int)(f * 19.0F);
                    context.drawTexture(ICONS, o, n, 0, 94, 18, 18);
                    context.drawTexture(ICONS, o, n + 18 - p, 18, 112 - p, 18, p);
                }
            }

            RenderSystem.disableBlend();
        }
    }

    @Inject(at = @At("HEAD"), method = "renderScoreboardSidebar", cancellable = true)
    public void renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci)
    {
        ci.cancel();

        var textRenderer = MinecraftClient.getInstance().textRenderer;

        Scoreboard scoreboard = objective.getScoreboard();
        Collection<ScoreboardPlayerScore> allScores = scoreboard.getAllPlayerScores(objective);

        List<ScoreboardPlayerScore> filteredScores = (List<ScoreboardPlayerScore>) allScores.stream()
                .filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#"))
                .collect(Collectors.toList());

        Collection<ScoreboardPlayerScore> displayedScores;
        if (filteredScores.size() > 15) {
            displayedScores = Lists.newArrayList(Iterables.skip(filteredScores, filteredScores.size() - 15));
        } else {
            displayedScores = filteredScores;
        }

        List<Pair<ScoreboardPlayerScore, Text>> scoreEntries = Lists.newArrayListWithCapacity(displayedScores.size());
        Text objectiveTitle = objective.getDisplayName();
        int titleWidth = textRenderer.getWidth(objectiveTitle);
        int maxWidth = titleWidth;
        int colonWidth = textRenderer.getWidth(": ");

        for (ScoreboardPlayerScore score : displayedScores) {
            Team team = scoreboard.getPlayerTeam(score.getPlayerName());
            Text formattedName = Team.decorateName(team, Text.literal(score.getPlayerName()));
            scoreEntries.add(Pair.of(score, formattedName));

            int entryWidth = textRenderer.getWidth(formattedName) +
                    colonWidth +
                    textRenderer.getWidth(Integer.toString(score.getScore()));
            maxWidth = Math.max(maxWidth, entryWidth);
        }

        int entryCount = displayedScores.size();
        int totalHeight = entryCount * 9;
        int centerY = this.scaledHeight / 2 + totalHeight / 3;
        int padding = 3;
        int scoreboardRight = this.scaledWidth - padding;
        int scoreboardLeft = scoreboardRight - maxWidth - padding * 2;
        int currentEntryIndex = 0;

        int rowBackgroundColor = MinecraftClient.getInstance().options.getTextBackgroundColor(0.4F);
        int headerBackgroundColor = MinecraftClient.getInstance().options.getTextBackgroundColor(0.4F);

        for (Pair<ScoreboardPlayerScore, Text> entry : scoreEntries) {
            currentEntryIndex++;
            ScoreboardPlayerScore score = entry.getFirst();
            Text playerNameText = entry.getSecond();

            Formatting scoreColor = Formatting.RED;
            String scoreText = "" + scoreColor + score.getScore();

            int rowY = centerY - currentEntryIndex * 9;
            int rowRightEdge = this.scaledWidth - padding + 2;
            int rowLeftEdge = scoreboardLeft - 2;

            enhancer$scoreboard$renderBackground(context, rowLeftEdge, rowY, rowRightEdge, rowY + 9, rowBackgroundColor);
            context.drawText(textRenderer, playerNameText, scoreboardLeft, rowY, -1, false);

            int scoreTextWidth = textRenderer.getWidth(scoreText);
            context.drawText(textRenderer, scoreText, rowRightEdge - scoreTextWidth, rowY, -1, false);

            if (currentEntryIndex == displayedScores.size()) {
                int headerY = rowY - 9 - 1;

                enhancer$scoreboard$renderHeader(context, rowLeftEdge, headerY, rowRightEdge, rowY - 1, headerBackgroundColor);
                enhancer$scoreboard$renderSeparator(context, scoreboardLeft, rowY, rowRightEdge, rowBackgroundColor);

                int titleX = scoreboardLeft + maxWidth / 2 - titleWidth / 2;
                context.drawText(textRenderer, objectiveTitle, titleX, rowY - 9, -1, false);

                ScoreboardRenderHook.RENDER_END.emit(null);
            }
        }
    }

    @Unique
    private void enhancer$scoreboard$renderSeparator(DrawContext context, int scoreboardLeft, int rowY, int rowRightEdge, int rowBackgroundColor) {
        var cancel = ScoreboardRenderHook.RENDER_SEPARATOR.emit(new ScoreboardRenderHookData(
                context,
                scoreboardLeft,
                rowY,
                rowRightEdge,
                rowY,
                rowBackgroundColor));

        if(!cancel) context.fill(scoreboardLeft - 2, rowY - 1, rowRightEdge, rowY, rowBackgroundColor);
    }

    @Unique
    private void enhancer$scoreboard$renderBackground(DrawContext context,
                                                      int left, int top,
                                                      int right, int bottom,
                                                      int backgroundColor) {
        var cancel = ScoreboardRenderHook.RENDER_BACKGROUND.emit(new ScoreboardRenderHookData(
                context, left, top, right, bottom, backgroundColor));

        if(!cancel) context.fill(left, top, right, bottom, backgroundColor);
    }

    @Unique
    private void enhancer$scoreboard$renderHeader(DrawContext context,
                                                  int left, int top,
                                                  int right, int bottom,
                                                  int headerColor) {
        var cancel = ScoreboardRenderHook.RENDER_HEADER.emit(new ScoreboardRenderHookData(
                context, left, top, right, bottom, headerColor));

        if(!cancel) context.fill(left, top, right, bottom, headerColor);
    }
}
