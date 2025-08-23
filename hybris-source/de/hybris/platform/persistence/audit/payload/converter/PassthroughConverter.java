package de.hybris.platform.persistence.audit.payload.converter;

public class PassthroughConverter implements PayloadConverter<Object>
{
    public String convertToString(Object obj)
    {
        return obj.toString();
    }


    public Object convertFromString(String str)
    {
        return str;
    }


    public Class<Object> forClass()
    {
        return Object.class;
    }
}
