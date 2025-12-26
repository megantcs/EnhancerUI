package ru.megantcs.enhancer.hook.data;

import net.minecraft.client.gui.DrawContext;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportField;

public record HotbarRenderHookData(DrawContext context,
                                   @LuaExportField(name = "left") int left,
                                   @LuaExportField(name = "top") int top,
                                   @LuaExportField(name = "right") int right,
                                   @LuaExportField(name = "bottom") int bottom,
                                   @LuaExportField(name = "rgb") int color) {
}
