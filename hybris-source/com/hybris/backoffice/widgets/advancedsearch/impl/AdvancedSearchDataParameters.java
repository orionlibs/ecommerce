/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;

/**
 * POJO class which contain parameters which can be passed to {@link AdvancedSearchData#parameters}
 */
public class AdvancedSearchDataParameters
{
    private final String typeCode;
    private final Map<String, String> parameters;


    public AdvancedSearchDataParameters(final String typeCode, final Map<String, String> parameters)
    {
        Preconditions.checkNotNull(typeCode);
        Preconditions.checkNotNull(parameters);
        this.typeCode = typeCode;
        this.parameters = parameters;
    }


    public String getTypeCode()
    {
        return typeCode;
    }


    public Map<String, String> getParameters()
    {
        return ImmutableMap.copyOf(parameters);
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
        final AdvancedSearchDataParameters that = (AdvancedSearchDataParameters)o;
        return Objects.equals(typeCode, that.typeCode) && Objects.equals(parameters, that.parameters);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(typeCode, parameters);
    }


    @Override
    public String toString()
    {
        return "AdvancedSearchDataParameters{" + "typeCode='" + typeCode + '\'' + ", parameters=" + parameters + '}';
    }
}
