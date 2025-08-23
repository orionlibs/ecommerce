/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels;

public final class LabelUtils
{
    private static final String OPEN = "[";
    private static final String CLOSE = "]";


    private LabelUtils()
    {
        //Utility class
    }


    /**
     * Returns the labelKey in brackets. In example '[my.label]'.
     *
     * @param labelKey
     * @return String
     */
    public static String getFallbackLabel(final String labelKey)
    {
        return OPEN + labelKey + CLOSE;
    }
}
