package de.hybris.platform.cms2.common.functions.impl;

import de.hybris.platform.cms2.common.functions.Converter;

public class BooleanValueFlexibleSearchConverter implements Converter<String, String>
{
    public static final String FLEXIBLE_SEARCH_BOOLEAN_TRUE = "1";
    public static final String FLEXIBLE_SEARCH_BOOLEAN_FALSE = "0";


    public String convert(String source)
    {
        return source.equals(Boolean.TRUE.toString()) ? "1" : "0";
    }
}
