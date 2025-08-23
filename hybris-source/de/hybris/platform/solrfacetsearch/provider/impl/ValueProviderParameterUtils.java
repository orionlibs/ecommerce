package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public final class ValueProviderParameterUtils
{
    public static int getInt(IndexedProperty indexedProperty, String key, int defaultValue)
    {
        int value = defaultValue;
        String stringValue = getStringValue(indexedProperty, key);
        if(stringValue != null)
        {
            value = Integer.parseInt(stringValue);
        }
        return value;
    }


    public static long getLong(IndexedProperty indexedProperty, String key, long defaultValue)
    {
        long value = defaultValue;
        String stringValue = getStringValue(indexedProperty, key);
        if(stringValue != null)
        {
            value = Long.parseLong(stringValue);
        }
        return value;
    }


    public static double getDouble(IndexedProperty indexedProperty, String key, double defaultValue)
    {
        double value = defaultValue;
        String stringValue = getStringValue(indexedProperty, key);
        if(stringValue != null)
        {
            value = Double.parseDouble(stringValue);
        }
        return value;
    }


    public static boolean getBoolean(IndexedProperty indexedProperty, String key, boolean defaultValue)
    {
        boolean value = defaultValue;
        String stringValue = getStringValue(indexedProperty, key);
        if(stringValue != null)
        {
            value = Boolean.parseBoolean(stringValue);
        }
        return value;
    }


    public static String getString(IndexedProperty indexedProperty, String key, String defaultValue)
    {
        String value = defaultValue;
        String stringValue = getStringValue(indexedProperty, key);
        if(stringValue != null)
        {
            value = stringValue;
        }
        return value;
    }


    protected static String getStringValue(IndexedProperty indexedProperty, String key)
    {
        String stringValue = null;
        Map<String, String> parameters = indexedProperty.getValueProviderParameters();
        if(parameters != null)
        {
            stringValue = StringUtils.trimToNull(parameters.get(key));
        }
        return stringValue;
    }
}
