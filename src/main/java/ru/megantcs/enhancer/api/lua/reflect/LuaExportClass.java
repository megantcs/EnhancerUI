package ru.megantcs.enhancer.api.lua.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LuaExportClass
{
    String name() default "";
}
