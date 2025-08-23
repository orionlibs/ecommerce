package de.hybris.platform.persistence.audit.payload.converter;

public class DoublePayloadConverter implements PayloadConverter<Double>
{
    public String convertToString(Double obj)
    {
        return obj.toString();
    }


    public Double convertFromString(String str)
    {
        return Double.valueOf(str);
    }


    public Class<Double> forClass()
    {
        return Double.class;
    }
}
