package ru.megantcs.enhancer.loader.MixinLoader;

import ru.megantcs.enhancer.api.lua.LuaScriptEngine;
import ru.megantcs.enhancer.api.lua.toolkit.LuaMethod;
import ru.megantcs.enhancer.api.lua.toolkit.PosObject;
import ru.megantcs.enhancer.hook.CrosshairRenderHook;
import ru.megantcs.enhancer.hook.data.CrosshairRenderHookData;

public class CrosshairRenderMixin extends LuaMixinModule
{
    private LuaScriptEngine luaScriptEngine;
    private LuaMethod method;

    @Override
    public void init(LuaScriptEngine sandbox) {
        method = null;
        this.luaScriptEngine = sandbox;
        CrosshairRenderHook.CROSSHAIR_RENDER.register("crosshair$render$hook", this::render);
    }

    public boolean render(CrosshairRenderHookData data) {
        if(method == null) return false;
        method.call(new PosObject(data.x(), data.y(), data.width(), data.height()));
        return true;
    }

    @Override
    public void shutdown() {
        method = null;
        CrosshairRenderHook.CROSSHAIR_RENDER.unregister("crosshair$render$hook");
    }

    @Override
    public void onChunkUpdate()
    {
        var method = luaScriptEngine.getMethod("mixin_crosshair_render");
        if(method != null) {
            this.method = new LuaMethod(method);
        }
    }
}
