package ru.megantcs.enhancer.platform.toolkit.events;

import org.jetbrains.annotations.Nullable;
import ru.megantcs.enhancer.platform.toolkit.events.api.Event;
import ru.megantcs.enhancer.platform.toolkit.events.impl.ActionEvent;
import ru.megantcs.enhancer.platform.toolkit.interfaces.Action;

import java.util.Objects;

public class EventChangedField<FieldType>
{
    private FieldType data;

    public EventChangedField() {
        data = null;
    }

    public EventChangedField(@Nullable FieldType base) {
        data = base;
    }

    public FieldType getValue() {
        return data;
    }

    public void setValue(FieldType value) {
        data = value;
        ChangedValue.invoker().invoke(value);
    }

    public Event<Action<FieldType>> ChangedValue = EventFactory.makeActionEvent();
}
