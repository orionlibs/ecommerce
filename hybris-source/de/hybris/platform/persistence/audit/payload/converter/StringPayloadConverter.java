package de.hybris.platform.persistence.audit.payload.converter;

public class StringPayloadConverter implements PayloadConverter<String>
{
    public String convertToString(String obj)
    {
        return obj;
    }


    public String convertFromString(String str)
    {
        return str;
    }


    public Class<String> forClass()
    {
        return String.class;
    }
}
