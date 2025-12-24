package ru.megantcs.enhancer.api.lua.reflect.wrappers;

import org.luaj.vm2.lib.VarArgFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Wrapper<Annotation, ReflectType> extends VarArgFunction
{
    protected final Logger logger;
    protected final Annotation annotation;
    protected final ReflectType reflectType;
    protected final Object instance;

    public Wrapper(Annotation annotation, ReflectType field, Object instance) {
        this.instance = instance;
        this.reflectType = field;
        this.annotation = annotation;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
}
