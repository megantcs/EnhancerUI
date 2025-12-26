package ru.megantcs.enhancer.hook.data;

import net.minecraft.client.gui.DrawContext;

public record BossBarRenderData(DrawContext context, float x, float y, float width, float height) {}