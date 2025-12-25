package ru.megantcs.enhancer.platform.toolkit.events.impl;

import ru.megantcs.enhancer.platform.toolkit.events.api.Event;
import ru.megantcs.enhancer.platform.toolkit.interfaces.Action;

import java.util.List;
import java.util.Objects;

public class ActionEvent<T> extends Event<Action<T>>
{
    private final List<ActionEventData<T>> subscribes;

    public ActionEvent(List<ActionEventData<T>> listType) {
        subscribes = listType;
        this.invoker = this::emit;
    }

    @Override
    public boolean register(Action<T> tAction) {
        if(contains(tAction)) return false;

        subscribes.add(new ActionEventData<>("actio#nevent@" + tAction.hashCode(), tAction));
        return true;
    }

    public boolean contains(Action<T> tAction) {
        for (ActionEventData<T> subscribe : subscribes) {
            if(subscribe.sub == tAction) return true;
        }

        return false;
    }

    public void emit(T arg) {
        subscribes.forEach((e)->{
            e.sub.invoke(arg);
        });
    }

    public boolean contains(String name) {
        for (ActionEventData<T> subscribe : subscribes) {
            if(Objects.equals(subscribe.name, name)) return true;
        }

        return false;
    }

    @Override
    public boolean register(String name, Action<T> handler) {
        if(contains(name) || contains(handler)) return false;

        subscribes.add(new ActionEventData<>(name, handler));
        return true;
    }

    @Override
    public boolean unregister(Action<T> tAction) {
        return subscribes.removeIf((e)->e.sub.equals(tAction));
    }

    @Override
    public boolean unregister(String name) {
        return subscribes.removeIf((e)-> Objects.equals(e.name, name));
    }

    private record ActionEventData<T>(String name, Action<T> sub) {}
}
