package ru.megantcs.ImHudEditor.platform.utils.interfaces;

import ru.megantcs.ImHudEditor.api.Events.api.Event;
import ru.megantcs.ImHudEditor.api.Events.impl.EventFactory;

import java.util.Objects;

public class EventChangedField<FieldType>
{
    FieldType data;

    public EventChangedField() {
        data = null;
    }

    public EventChangedField(FieldType base) {
        data = Objects.requireNonNull(base);
    }

    public FieldType getValue() {
        return data;
    }

    public void setValue(FieldType value) {
        data = value;
        ChangedValue.emit(value);
    }

    public Event<FieldType> ChangedValue = EventFactory.create();
}
