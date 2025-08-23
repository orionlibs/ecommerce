package de.hybris.platform.persistence.audit.payload.converter;

import java.util.Date;

public class DatePayloadConverter implements PayloadConverter<Date>
{
    public String convertToString(Date obj)
    {
        return String.valueOf(obj.getTime());
    }


    public Date convertFromString(String str)
    {
        return new Date(Long.parseLong(str));
    }


    public Class<Date> forClass()
    {
        return Date.class;
    }
}
