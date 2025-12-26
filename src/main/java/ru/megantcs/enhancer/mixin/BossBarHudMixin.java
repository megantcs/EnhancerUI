package ru.megantcs.enhancer.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.megantcs.enhancer.hook.BossBarRenderHook;
import ru.megantcs.enhancer.hook.data.BossBarRenderData;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Mixin(BossBarHud.class)
public abstract class BossBarHudMixin
{
    @Shadow
    @Final
    private static Identifier BARS_TEXTURE;

    @Shadow
    @Final
    private Map<UUID, ClientBossBar> bossBars;

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "render")
    public void render(DrawContext context, CallbackInfo ci) {
        if (!bossBars.isEmpty()) {
            int i = context.getScaledWindowWidth();
            int j = 12;

            for(ClientBossBar clientBossBar : this.bossBars.values()) {
                int k = i / 2 - 91;
                renderBossBarInternal(context, k, j, clientBossBar);
                Text text = clientBossBar.getName();
                int m = client.textRenderer.getWidth(text);
                int n = i / 2 - m / 2;
                int o = j - 9;
                context.drawTextWithShadow(this.client.textRenderer, text, n, o, 16777215);
                Objects.requireNonNull(this.client.textRenderer);
                j += 10 + 9;
                if (j >= context.getScaledWindowHeight() / 3) {
                    break;
                }
            }
        }
    }

    private void renderBossBarInternal(DrawContext context, int x, int y, BossBar bossBar) {
        renderBossBarPart(context, x, y, bossBar, 182, 0);

        int i = (int)(bossBar.getPercent() * 183.0F);
        if (i > 0) {
            renderBossBarPart(context, x, y, bossBar, i, 5);
        }
    }

    private void renderBossBarPart(DrawContext context, int x, int y, BossBar bossBar, int width, int height) {
        if (height == 0) {
            // ФОН
            var cancel = BossBarRenderHook.RENDER_BACKGROUND.emit(
                    new BossBarRenderData(context, x, y, width, 5)
            );
            if(!cancel) {
                context.drawTexture(BARS_TEXTURE, x, y, 0, bossBar.getColor().ordinal() * 5 * 2 + height, width, 5);
            }
        } else if (height == 5) {
            var cancel = BossBarRenderHook.RENDER_PROGRESS.emit(
                    new BossBarRenderData(context, x, y, width, 5)
            );
            if(!cancel) {
                context.drawTexture(BARS_TEXTURE, x, y, 0, bossBar.getColor().ordinal() * 5 * 2 + height, width, 5);
            }
        }

        if (bossBar.getStyle() != BossBar.Style.PROGRESS) {
            RenderSystem.enableBlend();
            context.drawTexture(BARS_TEXTURE, x, y, 0, 80 + (bossBar.getStyle().ordinal() - 1) * 5 * 2 + height, width, 5);
            RenderSystem.disableBlend();
        }
    }
}