package de.hybris.platform.persistence.audit.payload.converter;

public class FloatPayloadConverter implements PayloadConverter<Float>
{
    public String convertToString(Float obj)
    {
        return obj.toString();
    }


    public Float convertFromString(String str)
    {
        return Float.valueOf(str);
    }


    public Class<Float> forClass()
    {
        return Float.class;
    }
}
