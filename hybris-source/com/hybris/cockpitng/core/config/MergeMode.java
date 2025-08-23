/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

import java.util.Locale;

/**
 * Merge mode enumeration.
 */
public enum MergeMode
{
    /**
     * Objects will be merged - if matching object exists - or appended otherwise.
     */
    MERGE,
    /**
     * Object will replace matching object - if matching object exists - or appended otherwise.
     */
    REPLACE,
    /**
     * Matching object will be removed - if matching object exists - otherwise nothing will be removed or appended.
     */
    REMOVE;


    public static MergeMode get(final String mergeModeString)
    {
        if(MERGE.toString().equalsIgnoreCase(mergeModeString))
        {
            return MERGE;
        }
        else if(REPLACE.toString().equalsIgnoreCase(mergeModeString))
        {
            return REPLACE;
        }
        else if(REMOVE.toString().equalsIgnoreCase(mergeModeString))
        {
            return REMOVE;
        }
        else
        {
            return null;
        }
    }


    @Override
    public String toString()
    {
        return super.toString().toLowerCase(Locale.getDefault());
    }
}
