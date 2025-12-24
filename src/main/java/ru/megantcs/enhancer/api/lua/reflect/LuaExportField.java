package ru.megantcs.enhancer.api.lua.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LuaExportField
{
    String name();

    boolean read() default true;
    boolean write() default false;
}
