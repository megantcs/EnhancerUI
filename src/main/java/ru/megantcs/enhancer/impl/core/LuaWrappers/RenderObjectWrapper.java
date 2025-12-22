package ru.megantcs.enhancer.impl.core.lua;

import ru.megantcs.enhancer.api.lua.reflect.LuaExportClass;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportMethod;
import ru.megantcs.enhancer.impl.core.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.colors.Brush;
import ru.megantcs.enhancer.platform.toolkit.colors.ColorConvertor;

import java.util.Objects;

@LuaExportClass(name = "RenderObject") // for lua
public class RenderObjectWrapper
{
    private final RenderObject renderObject;

    @LuaExportMethod(name = "new")
    public RenderObjectWrapper(RenderObject renderObject) {
        this.renderObject = Objects.requireNonNull(renderObject, "renderObject, cannot be null");
    }

    @LuaExportMethod(name = "drawRect")
    public void drawRect(float x, float y, float z, float width, float height, float radius, float alpha,
                         String hex1, String hex2, String hex3, String hex4) {
        renderObject.drawRect(x, y, z, width, height, radius, alpha, new Brush(
                ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));
    }

    @LuaExportMethod(name = "drawBlur")
    public void drawBlur(float x, float y, float z, float width, float height, float radius, float alpha, float blur,
                         String hex1, String hex2, String hex3, String hex4) {
        renderObject.drawBlur(x, y, z, width, height, radius, alpha, blur, new Brush(
                ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));
    }

    @LuaExportMethod(name = "drawGlow")
    public void drawGlow(float x, float y, float z, float width, float height, float radius,
                         String hex1, String hex2, String hex3, String hex4)
    {
        renderObject.drawGlow(x, y, z, width, height, radius, new Brush(
                ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));
    }

    @LuaExportMethod(name = "drawBorder")
    public void drawBorder(float x, float y, float z, float width, float height, float radius, float border, float alpha,
                           String hex1, String hex2, String hex3, String hex4) {
        renderObject.drawBorder(x, y, z, width, height, radius, border, alpha, new Brush(
                ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));
    }

    @LuaExportMethod(name = "drawContourBorder")
    public void drawContourBorder(float x, float y, float z, float width, float height, float radius, float thickness, float dash, float gap, float alpha,
                                  String hex1, String hex2, String hex3, String hex4) {
        renderObject.drawContourBorder(x, y, z, width, height, radius, thickness, dash, gap,new Brush(
                ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));
    }
}
