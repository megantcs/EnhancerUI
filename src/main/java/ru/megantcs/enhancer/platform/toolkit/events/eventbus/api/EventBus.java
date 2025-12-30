package ru.megantcs.enhancer.platform.toolkit.events.eventbus.api;

import ru.megantcs.enhancer.platform.toolkit.exceptions.container.api.ExceptionContainer;
import ru.megantcs.enhancer.platform.toolkit.reflect.AnnotationSearcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public interface EventBus
{
    boolean register(Object entity);

    void post(Object event);

    class BaseEventBus implements EventBus
    {
        private final Map<Object, List<BaseEventHandler>> handlers = new ConcurrentHashMap<>();
        private final ExceptionContainer exceptionContainer;

        protected BaseEventBus(ExceptionContainer exceptionContainer)
        {
            this.exceptionContainer = Objects.requireNonNull(exceptionContainer, "cannot be null");
        }

        @Override
        public boolean register(Object entity)
        {
            Objects.requireNonNull(entity);
            var methods = AnnotationSearcher.getMethods(entity.getClass(), EventSubscribe.class);
            for (Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if(parameterTypes.length != 1) {
                    exceptionContainer.add(getClass(),
                            new IncompatibleClassChangeError("allowed number of arguments: 1"));
                    continue;
                }
                Class<?> t = parameterTypes[0];
                method.setAccessible(true);

                BaseEventHandler newHandler = event -> {
                    try {
                        method.invoke(entity, event);
                    } catch (Exception e) {
                        exceptionContainer.add(getClass(), e);
                    }
                };

                handlers.computeIfAbsent(t,
                        l -> new CopyOnWriteArrayList<>()).add(newHandler);
            }
            return false;
        }

        @Override
        public void post(Object event) {
            Objects.requireNonNull(event);
            Class<?> eventClass = event.getClass();
            List<BaseEventHandler> eventHandlers = handlers.get(eventClass);

            if (eventHandlers == null || eventHandlers.isEmpty()) return;
            List<BaseEventHandler> handlersCopy = new ArrayList<>(eventHandlers);

            for (BaseEventHandler handler : handlersCopy) {
                try {
                    handler.invoke(event);
                } catch (Exception e) {
                    exceptionContainer.add(getClass(), e);
                }
            }
        }

        @FunctionalInterface
        interface BaseEventHandler {
            void invoke(Object event);
        }
    }
}
