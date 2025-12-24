package ru.megantcs.enhancer.platform.toolkit.api;

public @interface AccessExceptions
{
    Class<? extends Exception>[] access();
}
