package ru.megantcs.enhancer.loader.MixinLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.api.lua.LuaScriptEngine;

public class LuaMixinModule
{
    protected final Logger logger;

    public LuaMixinModule() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void init(LuaScriptEngine sandbox) {}
    public void shutdown() {}
    public void onChunkUpdate() {}
}
