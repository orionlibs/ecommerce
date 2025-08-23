package de.hybris.platform.persistence.audit.payload.converter;

public class IntegerPayloadConverter implements PayloadConverter<Integer>
{
    public String convertToString(Integer obj)
    {
        return obj.toString();
    }


    public Integer convertFromString(String str)
    {
        return Integer.valueOf(str);
    }


    public Class<Integer> forClass()
    {
        return Integer.class;
    }
}
