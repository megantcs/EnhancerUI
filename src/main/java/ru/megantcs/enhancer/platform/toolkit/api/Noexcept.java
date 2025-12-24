package ru.megantcs.enhancer.platform.toolkit.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that when using the method, you do not need to handle the exception because it does
 * not throw it. The 'access' field can specify an exception that the method may throw, but it should
 * not be handled and the program should crash
 *
 * <p>Usage: in abstract classes or interfaces.
 * to specify conditions for implementing a method.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Noexcept
{
    Class<? extends Exception>[] access() default Exception.class;
}
