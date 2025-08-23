package de.hybris.platform.persistence.audit.payload.converter;

public class BytePayloadConverter implements PayloadConverter<Byte>
{
    public String convertToString(Byte obj)
    {
        return obj.toString();
    }


    public Byte convertFromString(String str)
    {
        return Byte.valueOf(str);
    }


    public Class<Byte> forClass()
    {
        return Byte.class;
    }
}
