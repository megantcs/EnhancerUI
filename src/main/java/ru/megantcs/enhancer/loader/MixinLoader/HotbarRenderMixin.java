package ru.megantcs.enhancer.loader.MixinLoader;

import ru.megantcs.enhancer.api.lua.LuaScriptEngine;
import ru.megantcs.enhancer.api.lua.toolkit.LuaMethod;
import ru.megantcs.enhancer.api.lua.toolkit.PosObject;
import ru.megantcs.enhancer.hook.HotBarRenderHook;
import ru.megantcs.enhancer.hook.ScoreboardRenderHook;
import ru.megantcs.enhancer.hook.handlers.HotBarPosHandler;

public class HotbarRenderMixin extends LuaMixinModule
{
    private LuaMethod render;
    private LuaMethod background;
    private LuaMethod select;

    private LuaScriptEngine sandbox;

    @Override
    public void init(LuaScriptEngine sandbox)
    {
        this.sandbox = sandbox;

        HotBarRenderHook.RENDER_BACKGROUND.register("mixin$hotbar$background", this::mixin$hotbar$background);
        HotBarRenderHook.RENDER_SELECT_SLOT.register("mixin$hotbar$select", this::mixin$hotbar$select);
    }

    private boolean mixin$hotbar$select(PosObject posObject)
    {
        boolean close = render != null;
        if(select != null) {
            select.call(posObject);
            close = true;
        }

        return close;
    }

    private boolean mixin$hotbar$background(ScoreboardRenderHook.RenderInfo renderInfo)
    {
        boolean close = false;
        if(background != null)
        {
            background.call(renderInfo);
            close = true;
        }
        if(render != null)
        {
            render.call(new PosObject(HotBarPosHandler.x, HotBarPosHandler.y,HotBarPosHandler.width, HotBarPosHandler.height));
            close = true;
        }
        return close;
    }

    @Override
    public void shutdown()
    {
        this.background = null;
        this.render = null;
        HotBarRenderHook.RENDER_BACKGROUND.unregister("mixin$hotbar$background");
        HotBarRenderHook.RENDER_SELECT_SLOT.unregister("mixin$hotbar$select");
    }

    @Override
    public void onChunkUpdate()
    {
        var render = sandbox.getMethod("handler_hotbar_render");
        if(render != null) {
            logger.warn("handler found: 'handler_hotbar_render'");
            this.render = new LuaMethod(render);
        }
        var background = sandbox.getMethod("mixin_hotbar_background");
        if(background != null) {
            logger.warn("mixin found: 'mixin_hotbar_render'");
            this.background = new LuaMethod(background);
        }
        var select = sandbox.getMethod("mixin_hotbar_select");
        if(select != null) {
            logger.warn("mixin found: 'mixin_hotbar_select'");
            this.select = new LuaMethod(select);
        }
    }
}
