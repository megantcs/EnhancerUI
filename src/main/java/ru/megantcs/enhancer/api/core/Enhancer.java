package ru.megantcs.enhancer.api.core;

import net.minecraft.client.util.math.MatrixStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.megantcs.enhancer.impl.core.RenderObject;

public abstract class Enhancer
{
    public final DrawCallback drawCallback;

    protected Logger LOGGER = LoggerFactory.getLogger(Enhancer.class);

    public abstract RenderObject instanceObj(MatrixStack matrixStack);

    public Enhancer() {
        drawCallback = new DrawCallback();
    }
}
