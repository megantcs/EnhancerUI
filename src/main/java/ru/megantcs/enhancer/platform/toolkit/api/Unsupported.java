package ru.megantcs.enhancer.platform.toolkit.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Unsupported
{
    String reason() default "None";
    String since() default "0";
}
