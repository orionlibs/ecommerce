/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataquality.model;

/**
 * Represents Data Quality Coverage property
 */
public class DataQualityProperty
{
    private final String propertyQualifier;
    private final String message;


    public DataQualityProperty(final String propertyQualifier, final String message)
    {
        this.propertyQualifier = propertyQualifier;
        this.message = message;
    }


    public String getPropertyQualifier()
    {
        return propertyQualifier;
    }


    public String getMessage()
    {
        return message;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        final DataQualityProperty that = (DataQualityProperty)o;
        if(!propertyQualifier.equals(that.propertyQualifier))
        {
            return false;
        }
        return message.equals(that.message);
    }


    @Override
    public int hashCode()
    {
        int result = propertyQualifier.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
