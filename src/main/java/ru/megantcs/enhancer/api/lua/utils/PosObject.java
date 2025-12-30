package ru.megantcs.enhancer.api.lua.toolkit;

import ru.megantcs.enhancer.api.lua.reflect.LuaExportField;

public record PosObject(@LuaExportField(name = "x") float x,
                        @LuaExportField(name = "y") float y,
                        @LuaExportField(name = "width") float width,
                        @LuaExportField(name = "height") float height)
{

}
