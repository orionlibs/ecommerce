package de.hybris.platform.persistence.audit.payload.converter;

public class LongPayloadConverter implements PayloadConverter<Long>
{
    public String convertToString(Long obj)
    {
        return String.valueOf(obj);
    }


    public Long convertFromString(String str)
    {
        return Long.valueOf(str);
    }


    public Class<Long> forClass()
    {
        return Long.class;
    }
}
