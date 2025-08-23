package com.hybris.backoffice.solrsearch.populators.conditionvalueconverters;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Function;

public class DateConditionValueConverter implements Function<Date, String>
{
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    public String apply(Date date)
    {
        return (date != null) ? this.dateTimeFormatter.format(date.toInstant().atZone(ZoneOffset.UTC)) : "";
    }
}
