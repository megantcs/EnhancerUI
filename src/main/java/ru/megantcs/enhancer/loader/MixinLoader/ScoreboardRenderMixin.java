package ru.megantcs.enhancer.loader.MixinLoader;

import ru.megantcs.enhancer.api.lua.LuaScriptEngine;
import ru.megantcs.enhancer.api.lua.toolkit.LuaMethod;
import ru.megantcs.enhancer.api.lua.toolkit.PosObject;
import ru.megantcs.enhancer.hook.ScoreboardRenderHook;
import ru.megantcs.enhancer.hook.handlers.ScoreboardPosHandler;
import ru.megantcs.enhancer.platform.toolkit.reflect.FinishedObjects.FinishField;

public class ScoreboardRenderMixin extends LuaMixinModule
{
    @FinishField private LuaMethod background;
    @FinishField private LuaMethod header;
    @FinishField private LuaMethod separator;
    @FinishField private LuaMethod render;

    private LuaScriptEngine sandbox;
    private boolean closeOtherRender = false;

    @Override
    public void init(LuaScriptEngine sandbox) {
        ScoreboardRenderHook.RENDER_BACKGROUND.register(this::scoreboard$background, "scoreboard$background");
        ScoreboardRenderHook.RENDER_HEADER.register(this::scoreboard$header, "scoreboard$header");
        ScoreboardRenderHook.RENDER_SEPARATOR.register(this::scoreboard$separator, "scoreboard$separator");

        this.sandbox = sandbox;
    }

    @Override
    public void shutdown() {
        ScoreboardRenderHook.RENDER_BACKGROUND.unregister("scoreboard$background");
        ScoreboardRenderHook.RENDER_HEADER.unregister("scoreboard$header");
        ScoreboardRenderHook.RENDER_SEPARATOR.unregister("scoreboard$separator");

        background = null;
        header = null;
        separator = null;
    }

    private boolean scoreboard$background(ScoreboardRenderHook.RenderInfo renderInfo) {
        boolean close = false;
        if(render != null) {
            render.call(new PosObject(
                    ScoreboardPosHandler.getX(),
                    ScoreboardPosHandler.getY(),
                    ScoreboardPosHandler.getWidth(),
                    ScoreboardPosHandler.getHeight()));
            close = true;
            closeOtherRender = true;
        }
        if(background != null) {
            background.call(renderInfo);
            close = true;
        }
        return close;
    }

    private boolean scoreboard$header(ScoreboardRenderHook.RenderInfo renderInfo) {
        if(header == null) return closeOtherRender;
        header.call(renderInfo);

        return closeOtherRender;
    }

    private boolean scoreboard$separator(ScoreboardRenderHook.RenderInfo renderInfo) {
        if(separator == null) return closeOtherRender;
        separator.call(renderInfo);

        return true;
    }

    @Override
    public void onChunkUpdate()
    {
        var background = sandbox.getMethod("mixin_scoreboard_background");
        var header = sandbox.getMethod("mixin_scoreboard_header");
        var separator = sandbox.getMethod("mixin_scoreboard_separator");
        var render = sandbox.getMethod("handler_scoreboard_render");

        if(render != null) {
            logger.warn("found handler: 'handler_scoreboard_render'");
            this.render = new LuaMethod(render);
        }
        if(background != null) {
            logger.warn("found mixin: 'mixin_scoreboard_background'");
            this.background = new LuaMethod(background);
        }
        if(header != null) {
            logger.warn("found mixin: 'mixin_scoreboard_header'");
            this.header = new LuaMethod(header);
        }
        if(separator != null) {
            logger.warn("found mixin: 'mixin_scoreboard_separator'");
            this.separator = new LuaMethod(separator);
        }
    }
}
