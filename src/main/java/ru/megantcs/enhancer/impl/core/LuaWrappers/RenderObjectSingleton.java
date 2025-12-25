package ru.megantcs.enhancer.impl.core.LuaWrappers;

import org.jetbrains.annotations.Nullable;
import ru.megantcs.enhancer.EnhancerPlatform;
import ru.megantcs.enhancer.api.core.Enhancer;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportClass;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportMethod;
import ru.megantcs.enhancer.impl.EnhancerInstance;
import ru.megantcs.enhancer.impl.core.EnhancerUI;
import ru.megantcs.enhancer.impl.core.RenderObject;

@LuaExportClass(name = "RenderObjectSingleton")
public class RenderObjectSingleton
{
    private static RenderObject renderObject;

    public static void setRenderObject(@Nullable RenderObject value) {
        renderObject = value;
    }

    @LuaExportMethod(name = "renderObject")
    public static RenderObjectWrapper getRenderObject() {
        return new RenderObjectWrapper(renderObject);
    }

    public static void init()
    {
        Enhancer enhancer = EnhancerInstance.create();
        enhancer.drawCallback.EVENT.register((e, t)->{
           renderObject = e;
        });
    }
}
