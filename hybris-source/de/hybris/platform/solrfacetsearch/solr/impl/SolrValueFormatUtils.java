package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceRuntimeException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class SolrValueFormatUtils
{
    private static final Map<String, Function<String, String>> FORMATTERS = new HashMap<>();

    static
    {
        FORMATTERS.put(generateKey(String.class), SolrValueFormatUtils::formatString);
        FORMATTERS.put(generateKey(Boolean.class), SolrValueFormatUtils::formatBoolean);
        FORMATTERS.put(generateKey(Short.class), SolrValueFormatUtils::formatShort);
        FORMATTERS.put(generateKey(Integer.class), SolrValueFormatUtils::formatInteger);
        FORMATTERS.put(generateKey(Long.class), SolrValueFormatUtils::formatLong);
        FORMATTERS.put(generateKey(Float.class), SolrValueFormatUtils::formatFloat);
        FORMATTERS.put(generateKey(Double.class), SolrValueFormatUtils::formatDouble);
        FORMATTERS.put(generateKey(BigInteger.class), SolrValueFormatUtils::formatBigInteger);
        FORMATTERS.put(generateKey(BigDecimal.class), SolrValueFormatUtils::formatBigDecimal);
        FORMATTERS.put(generateKey(Date.class), SolrValueFormatUtils::formatDate);
    }

    protected static String generateKey(Class<?> valueClass)
    {
        return valueClass.getCanonicalName();
    }


    public static String format(String value, Class<?> valueClass)
    {
        if(valueClass == null)
        {
            throw new IllegalArgumentException("targetClass cannot be null");
        }
        if(value == null)
        {
            return null;
        }
        String formatterKey = generateKey(valueClass);
        Function<String, String> formatter = FORMATTERS.get(formatterKey);
        if(formatter == null)
        {
            throw new SolrServiceRuntimeException("Cannot find formatter for " + valueClass.getCanonicalName());
        }
        return formatter.apply(value);
    }


    public static String formatString(String value)
    {
        return value;
    }


    public static String formatBoolean(String value)
    {
        return Boolean.valueOf(value).toString();
    }


    public static String formatShort(String value)
    {
        return Short.valueOf(value).toString();
    }


    public static String formatInteger(String value)
    {
        return Integer.valueOf(value).toString();
    }


    public static String formatLong(String value)
    {
        return Long.valueOf(value).toString();
    }


    public static String formatFloat(String value)
    {
        return Float.valueOf(value).toString();
    }


    public static String formatDouble(String value)
    {
        return Double.valueOf(value).toString();
    }


    public static String formatBigInteger(String value)
    {
        return (new BigInteger(value)).toString();
    }


    public static String formatBigDecimal(String value)
    {
        return (new BigDecimal(value)).toString();
    }


    public static String formatDate(String value)
    {
        OffsetDateTime dateTime = OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime utcDateTime = dateTime.withOffsetSameInstant(ZoneOffset.UTC);
        return utcDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
