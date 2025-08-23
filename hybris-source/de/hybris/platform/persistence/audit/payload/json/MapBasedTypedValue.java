package de.hybris.platform.persistence.audit.payload.json;

public class MapBasedTypedValue
{
    private ValueType type;
    private String value;


    public MapBasedTypedValue()
    {
    }


    public MapBasedTypedValue(ValueType type, String value)
    {
        this.type = type;
        this.value = value;
    }


    public ValueType getType()
    {
        return this.type;
    }


    public String getValue()
    {
        return this.value;
    }
}
