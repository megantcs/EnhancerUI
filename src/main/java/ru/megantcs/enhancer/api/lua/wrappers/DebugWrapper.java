package ru.megantcs.enhancer.api.lua.wrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportClass;
import ru.megantcs.enhancer.api.lua.reflect.LuaExportMethod;
import ru.megantcs.enhancer.platform.toolkit.api.Noexcept;

@LuaExportClass(name = "Debug")
public class DebugWrapper
{
    private static final Logger logger = LoggerFactory.getLogger("Debug");

    @LuaExportMethod @Noexcept
    public static void log(String message)
    {
        if(message == null) return;
        logger.info("~ {}",message);
    }

    @LuaExportMethod @Noexcept
    public static void err(String message)
    {
        if(message == null) return;
        logger.error("~ {}",message);
    }

    @LuaExportMethod @Noexcept
    public static void warn(String message)
    {
        if(message == null) return;
        logger.warn("~ {}",message);
    }
}
