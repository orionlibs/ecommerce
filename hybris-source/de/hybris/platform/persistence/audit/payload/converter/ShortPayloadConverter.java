package de.hybris.platform.persistence.audit.payload.converter;

public class ShortPayloadConverter implements PayloadConverter<Short>
{
    public String convertToString(Short obj)
    {
        return obj.toString();
    }


    public Short convertFromString(String str)
    {
        return Short.valueOf(str);
    }


    public Class<Short> forClass()
    {
        return Short.class;
    }
}
