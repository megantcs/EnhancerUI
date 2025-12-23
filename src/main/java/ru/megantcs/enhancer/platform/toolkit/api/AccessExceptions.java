package ru.megantcs.enhancer.platform.toolkit.reflect;

public @interface AccessExceptions
{
    Class<? extends Exception>[] access();
}
