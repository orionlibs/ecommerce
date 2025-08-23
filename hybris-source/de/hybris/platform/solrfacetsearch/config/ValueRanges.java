package de.hybris.platform.solrfacetsearch.config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ValueRanges
{
    public static final String INFINITY = "INF";
    public static final Set<String> ALLOWEDTYPES;
    public static final String DATEFORMAT = "yyyy-MM-dd [HH:mm]";

    static
    {
        ALLOWEDTYPES = Collections.unmodifiableSet(
                        (Set<? extends String>)Stream.<ValueRangeType>of(ValueRangeType.values()).map(type -> type.toString().toLowerCase(Locale.ROOT)).collect(Collectors.toSet()));
    }

    public static String getAllowedRangeTypes()
    {
        String separator = ", ";
        String result = "";
        for(String type : ALLOWEDTYPES)
        {
            if("".equals(result))
            {
                result = result.concat(type);
                continue;
            }
            result = result.concat(", " + type);
        }
        return result;
    }


    public static Date parseDate(String date) throws ParseException
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd [HH:mm]", Locale.getDefault());
        df.setLenient(false);
        try
        {
            return df.parse(date.replaceAll("(\\s)+", " "));
        }
        catch(ParseException e)
        {
            if(!date.contains("["))
            {
                return df.parse(date.intern().concat(" [00:00]"));
            }
            throw e;
        }
    }


    public static ValueRange createValueRange(String name, Object from, Object to)
    {
        ValueRange set = new ValueRange();
        set.setFrom((Comparable)from);
        set.setName(name);
        set.setTo((Comparable)to);
        return set;
    }
}
