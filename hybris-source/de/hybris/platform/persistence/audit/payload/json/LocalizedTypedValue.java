package de.hybris.platform.persistence.audit.payload.json;

import java.util.List;

public class LocalizedTypedValue
{
    private final ValueType type;
    private final List<LocalizedValue> values;


    public LocalizedTypedValue()
    {
        this.type = null;
        this.values = null;
    }


    public LocalizedTypedValue(ValueType type, List<LocalizedValue> values)
    {
        this.type = type;
        this.values = values;
    }


    public ValueType getType()
    {
        return this.type;
    }


    public List<LocalizedValue> getValues()
    {
        return this.values;
    }
}
