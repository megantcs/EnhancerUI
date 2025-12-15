package ru.megantcs.enhancer.impl;

import ru.megantcs.enhancer.api.core.Enhancer;
import ru.megantcs.enhancer.impl.core.EnhancerUI;

public class EnhancerInstance
{
    public static Enhancer create() {
        return new EnhancerUI();
    }
}
