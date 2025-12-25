package ru.megantcs.enhancer.platform.toolkit.events.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.megantcs.enhancer.platform.toolkit.events.api.Event;
import ru.megantcs.enhancer.platform.toolkit.interfaces.Func;

import java.util.List;
import java.util.Objects;

public class FuncEvent<ArgumentType, ReturnType>
        extends Event<Func<ArgumentType, ReturnType>>
{
    private final List<FuncEventData<ArgumentType, ReturnType>> subscribes;
    private final ReturnType defaultValue;

    public FuncEvent(List<FuncEventData<ArgumentType, ReturnType>> listType,
                     @Nullable ReturnType returnType) {
        subscribes = Objects.requireNonNull(listType);
        defaultValue = returnType;
        this.invoker = this::emit;
    }

    @Override
    public boolean register(@NotNull Func<ArgumentType, ReturnType> event) {
        subscribes.add(new FuncEventData<>(Objects.requireNonNull(event, "event cannot be null"), "func#event@" + event.hashCode()));
        return true;
    }

    @Override
    public boolean register(String name, Func<ArgumentType, ReturnType> sub) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(sub);

        if(contains(name) || contains(sub)) return false;

        return subscribes.add(new FuncEventData<>(sub, name));
    }

    private boolean contains(String name) {
        Objects.requireNonNull(name);
        for (FuncEventData<ArgumentType, ReturnType> subscribe : subscribes) {
            if(Objects.equals(subscribe.name, name)) return true;
        }

        return false;
    }

    private boolean contains(Func<ArgumentType, ReturnType> sub) {
        Objects.requireNonNull(sub);
        for (FuncEventData<ArgumentType, ReturnType> subscribe : subscribes) {
            if(subscribe.subscribe.equals(sub)) return true;
        }

        return false;
    }

    @Override
    public boolean unregister(@NotNull Func<ArgumentType, ReturnType> event) {
        Objects.requireNonNull(event, "event cannot be null");
        return subscribes.removeIf((data)-> data.subscribe == event);
    }

    @Override
    public boolean unregister(String name)
    {
        Objects.requireNonNull(name, "name cannot be null");
        return subscribes.removeIf((data) -> Objects.equals(data.name, name));
    }

    public ReturnType emit(ArgumentType argumentType) {
        var result = defaultValue;
        for (var data : subscribes) {
            var subscribe = data.subscribe;
            result = subscribe.run(argumentType);
            if(defaultValue != null &&
                    result == null) result = defaultValue;
        }
        return result;
    }

    record FuncEventData<ArgumentType, ReturnType>(Func<ArgumentType, ReturnType> subscribe, String name)
    {
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof String $)
                return Objects.equals(name, $);

            return subscribe == obj;
        }
    }
}
