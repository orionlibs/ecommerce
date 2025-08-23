package de.hybris.platform.persistence.audit.payload.converter;

public class BooleanPayloadConverter implements PayloadConverter<Boolean>
{
    public String convertToString(Boolean obj)
    {
        return obj.toString();
    }


    public Boolean convertFromString(String str)
    {
        return Boolean.valueOf(str);
    }


    public Class<Boolean> forClass()
    {
        return Boolean.class;
    }
}
