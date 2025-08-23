/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.fulltextsearch;

import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.util.type.BackofficeTypeUtils;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link SearchFilterValidationStrategy}.
 */
public class DefaultSearchFilterValidationStrategy implements SearchFilterValidationStrategy
{
    private BackofficeTypeUtils backofficeTypeUtils;
    private FullTextSearchStrategy fullTextSearchStrategy;
    private TypeFacade typeFacade;


    @Override
    public boolean isValid(final String typeCode, final String name, final Object value)
    {
        if(StringUtils.isEmpty(name))
        {
            return false;
        }
        if(value == null)
        {
            return true;
        }
        final Object filterValue = getFilterValue(typeCode, name, value);
        final String attributeTypeCode = getTypeFacade().getAttribute(typeCode, name).getDefinedType().getCode();
        final String filterValueTypeCode = getTypeFacade().getType(filterValue);
        return getBackofficeTypeUtils().isAssignableFrom(attributeTypeCode, filterValueTypeCode);
    }


    protected Object getFilterValue(final String typeCode, final String name, final Object value)
    {
        final boolean isFilterLocalized = getFullTextSearchStrategy().isLocalized(typeCode, name);
        final boolean isValueLocalized = value instanceof Map;
        if(isFilterLocalized && isValueLocalized && !((Map)value).isEmpty())
        {
            return ((Map)value).values().stream().findFirst().orElse(null);
        }
        return value;
    }


    @Override
    public boolean canHandle(final String preferredSearchStrategy)
    {
        return true;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public FullTextSearchStrategy getFullTextSearchStrategy()
    {
        return fullTextSearchStrategy;
    }


    @Required
    public void setFullTextSearchStrategy(final FullTextSearchStrategy fullTextSearchStrategy)
    {
        this.fullTextSearchStrategy = fullTextSearchStrategy;
    }


    public BackofficeTypeUtils getBackofficeTypeUtils()
    {
        return backofficeTypeUtils;
    }


    @Required
    public void setBackofficeTypeUtils(final BackofficeTypeUtils backofficeTypeUtils)
    {
        this.backofficeTypeUtils = backofficeTypeUtils;
    }
}
