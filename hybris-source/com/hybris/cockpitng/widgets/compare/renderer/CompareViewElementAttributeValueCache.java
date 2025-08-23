/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;

/**
 * This class represents the cache of compareView's element.
 */
public class CompareViewElementAttributeValueCache
{
    private final Object value;
    private final String qualifier;
    private final DataType dataType;


    /**
     * Construction method.
     *
     * @param value
     *           - value of compareView element
     * @param qualifier
     *           - qualifier of compareView element attribute
     * @param dataType
     *           - dataType of compareView element attribute
     */
    public CompareViewElementAttributeValueCache(final Object value, final String qualifier, final DataType dataType)
    {
        this.value = value;
        this.qualifier = qualifier;
        this.dataType = dataType;
    }


    /**
     * Returns the value of compareView element.
     *
     * @return the value of compareView element
     */
    public Object getValue()
    {
        return value;
    }


    /**
     * Returns the qualifier of compareView element attribute.
     *
     * @return the qualifier of compareView element attribute
     */
    public String getQualifier()
    {
        return qualifier;
    }


    /**
     * Returns the qualifier of compareView element attribute.
     *
     * @return the qualifier of compareView element attribute
     */
    public DataType getDataType()
    {
        return dataType;
    }


    /**
     * Whether it is equal to the given object.
     *
     * @param o
     *           - object being compared
     * @return whether it is equal to the given object
     */
    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final CompareViewElementAttributeValueCache that = (CompareViewElementAttributeValueCache)o;
        return value == that.value && qualifier.equals(that.qualifier) && dataType.equals(that.dataType);
    }


    /**
     * Returns the hashCode of compareView element attribute cache.
     *
     * @return the hashCode of compareView element attribute cache
     */
    @Override
    public int hashCode()
    {
        int result = value.hashCode();
        result = 31 * result + qualifier.hashCode();
        result = 31 * result + dataType.hashCode();
        return result;
    }
}
