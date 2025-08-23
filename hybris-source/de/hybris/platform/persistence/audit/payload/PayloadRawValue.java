package de.hybris.platform.persistence.audit.payload;

import de.hybris.platform.persistence.audit.payload.json.ValueType;
import java.util.List;

public class PayloadRawValue
{
    private final String name;
    private final List<String> values;
    private final String mapValue;
    private final ValueType type;
    private final String language;


    private PayloadRawValue(String name, List<String> values, String mapValue, ValueType type, String language)
    {
        this.name = name;
        this.values = values;
        this.mapValue = mapValue;
        this.type = type;
        this.language = language;
    }


    public static PayloadRawValue withListValue(String name, List<String> value, ValueType type, String language)
    {
        return new PayloadRawValue(name, value, null, type, language);
    }


    public static PayloadRawValue withListValue(String name, List<String> value, ValueType type)
    {
        return new PayloadRawValue(name, value, null, type, null);
    }


    public static PayloadRawValue withMapValue(String name, String mapValue, ValueType type, String language)
    {
        return new PayloadRawValue(name, null, mapValue, type, language);
    }


    public static PayloadRawValue withMapValue(String name, String mapValue, ValueType type)
    {
        return new PayloadRawValue(name, null, mapValue, type, null);
    }


    public String getName()
    {
        return this.name;
    }


    public List<String> getValues()
    {
        return this.values;
    }


    public String getMapValue()
    {
        return this.mapValue;
    }


    public boolean containsMapValue()
    {
        return (this.mapValue != null);
    }


    public String getLanguage()
    {
        return this.language;
    }


    public ValueType getValueType()
    {
        return this.type;
    }


    public String getType()
    {
        return (this.type != null) ? this.type.getType() : null;
    }
}
