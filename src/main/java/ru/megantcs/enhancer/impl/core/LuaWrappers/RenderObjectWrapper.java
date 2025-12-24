package ru.megantcs.enhancer.impl.core.LuaWrappers;

import org.jetbrains.annotations.Nullable;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportClass;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportMethod;
import ru.megantcs.enhancer.impl.core.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.colors.Brush;
import ru.megantcs.enhancer.platform.toolkit.colors.ColorConvertor;

@LuaExportClass(name = "RenderObject") // for lua
public class RenderObjectWrapper
{
    @Nullable
    private final RenderObject renderObject;

    @LuaExportMethod(name = "new") // for load class in lua-loader (sandbox)
    public RenderObjectWrapper() {
        renderObject = null;
    }

    @LuaExportMethod(name = "create") // not use from lua
    public RenderObjectWrapper(@Nullable RenderObject renderObject) {
        this.renderObject = renderObject;
    }

    @LuaExportMethod(name = "drawRect")
    public boolean drawRect(float x, float y, float z, float width, float height, float radius, float alpha,
                         String hex1, String hex2, String hex3, String hex4) {
        if(renderObject == null) return false;

        if(hex1 == null || hex2 == null
                || hex3 == null || hex4 == null) return false;

        renderObject.drawRect(x, y, z, width, height, radius, alpha, new Brush(
                ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));

        return true;
    }

    @LuaExportMethod(name = "drawBlur")
    public boolean drawBlur(float x, float y, float z, float width, float height, float radius, float alpha, float blur,
                         String hex1, String hex2, String hex3, String hex4) {
        if(renderObject == null) return false;

        if(hex1 == null || hex2 == null
                || hex3 == null || hex4 == null) return false;

        renderObject.drawBlur(x, y, z, width, height, radius, alpha, blur, new Brush(
                ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));

        return true;
    }

    @LuaExportMethod(name = "drawGlow")
    public boolean drawGlow(float x, float y, float z, float width, float height, float radius,
                         String hex1, String hex2, String hex3, String hex4)
    {
        if(renderObject == null) return false;

        if(hex1 == null || hex2 == null
                || hex3 == null || hex4 == null) return false;

        renderObject.drawGlow(x, y, z, width, height, radius, new Brush(
                ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));
        return true;
    }

    @LuaExportMethod(name = "drawBorder")
    public boolean drawBorder(float x, float y, float z, float width, float height, float radius, float border, float alpha,
                           String hex1, String hex2, String hex3, String hex4) {
        if(renderObject == null) return false;

        if(hex1 == null || hex2 == null
                || hex3 == null || hex4 == null) return false;

        renderObject.drawBorder(x, y, z, width, height, radius, border, alpha, new Brush(
                ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));
        return true;
    }

    @LuaExportMethod(name = "drawContourBorder")
    public boolean drawContourBorder(float x, float y, float z, float width, float height, float radius, float thickness, float dash, float gap, float alpha,
                                  String hex1, String hex2, String hex3, String hex4) {
        if(renderObject == null) return false;

        if(hex1 == null || hex2 == null
                || hex3 == null || hex4 == null) return false;

        renderObject.drawContourBorder(x, y, z, width, height, radius, thickness, dash, gap,new Brush(
                ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));

        return true;
    }
}
