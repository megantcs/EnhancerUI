package ru.megantcs.enhancer.platform.toolkit.events;

import ru.megantcs.enhancer.platform.toolkit.events.api.Event;
import ru.megantcs.enhancer.platform.toolkit.events.impl.ActionEvent;
import ru.megantcs.enhancer.platform.toolkit.events.impl.BackendArrayEvent;
import ru.megantcs.enhancer.platform.toolkit.events.impl.FuncEvent;
import ru.megantcs.enhancer.platform.toolkit.events.impl.RunnableEvent;
import ru.megantcs.enhancer.platform.toolkit.interfaces.Func;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventFactory
{
    public static  <T> Event<T> makeEvent(Class<T> type, Func<T[], T> invoker) {
        return new BackendArrayEvent<>(type, invoker);
    }

    public static  RunnableEvent makeRunnableEvent(List<RunnableEvent.RunnableEventData> listType) {
        return new RunnableEvent(listType);
    }

    public static  RunnableEvent makeRunnableEvent() {
        return new RunnableEvent(new ArrayList<>());
    }

    public static  RunnableEvent makeRunnableEventSync() {
        return makeRunnableEvent(new CopyOnWriteArrayList<>());
    }

    public static  RunnableEvent makeRunnableEventArray() {
        return new RunnableEvent(new ArrayList<>());
    }

    public static <T1, T2> FuncEvent<T1, T2> makeFuncEvent() {
        return new FuncEvent<>(new CopyOnWriteArrayList<>(), null);
    }

    public static <T1, T2> FuncEvent<T1, T2> makeFuncEvent(T2 defaultReturnType) {
        return new FuncEvent<>(new CopyOnWriteArrayList<>(), defaultReturnType);
    }

    public static <T1, T2> FuncEvent<T1, T2> makeFuncEventArray(T2 defaultReturnType) {
        return new FuncEvent<>(new ArrayList<>(), defaultReturnType);
    }

    public static <T> ActionEvent<T> makeActionEvent() {return new ActionEvent<>(new CopyOnWriteArrayList<>());}
}
