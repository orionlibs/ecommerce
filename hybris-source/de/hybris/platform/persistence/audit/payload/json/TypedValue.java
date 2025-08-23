package de.hybris.platform.persistence.audit.payload.json;

import java.util.List;

public class TypedValue
{
    private final ValueType type;
    private final List<String> value;


    public TypedValue()
    {
        this.type = null;
        this.value = null;
    }


    public TypedValue(ValueType type, List<String> value)
    {
        this.type = type;
        this.value = value;
    }


    public ValueType getType()
    {
        return this.type;
    }


    public List<String> getValue()
    {
        return this.value;
    }
}
