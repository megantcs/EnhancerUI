package ru.megantcs.enhancer.api.core;

import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.platform.toolkit.Warnings;
import ru.megantcs.enhancer.platform.toolkit.api.AccessExceptions;
import ru.megantcs.enhancer.platform.toolkit.interfaces.Returnable;

import java.util.Objects;

public record PartPipeline<ResultType
        extends PartPipeline.PartPipelineSupported<ResultType>>(ResultType baseData)
        implements Returnable<ResultType> {
    public PartPipeline(ResultType baseData) {
        this.baseData = Objects.requireNonNull(baseData);
    }

    public PartPipeline<ResultType> part(Returnable<ResultType> add) {
        Objects.requireNonNull(add);

        baseData.paste(add.get());
        return this;
    }

    public PartPipeline<ResultType> part(ResultType add) {
        Objects.requireNonNull(add);

        baseData.paste(add);
        return this;
    }

    @Override
    public ResultType get() {
        return baseData;
    }


    @FunctionalInterface
    public interface PartPipelineSupported<T> {
        @AccessExceptions(access = NullPointerException.class)
        @SuppressWarnings(Warnings.unusedReturnValue)
        T paste(@NotNull T item);
    }
}
