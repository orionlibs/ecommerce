/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.search.core.search;

import com.hybris.backoffice.widgets.fulltextsearch.FullTextSearchStrategy;
import com.hybris.backoffice.widgets.fulltextsearch.SearchFilterValidationStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.search.data.ValueComparisonOperator;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link SearchFilterValidationStrategy} using Solr and Search Services engine.
 */
public class BackofficeSearchFilterValidationStrategy implements SearchFilterValidationStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeSearchFilterValidationStrategy.class);
    private FullTextSearchStrategy searchStrategy;
    private TypeFacade typeFacade;


    @Override
    public boolean isValid(final String typeCode, final String name, final Object value, final ValueComparisonOperator operator)
    {
        final boolean isLocalized = (value == null)
                        || (value instanceof Map && ((Map)value).isEmpty() && isLocalizedProperty(typeCode, name));
        return (!operator.isRequireValue() && isLocalized) || isValid(typeCode, name, value);
    }


    protected boolean isLocalizedProperty(final String typeCode, final String name)
    {
        try
        {
            return getTypeFacade().load(typeCode).getAttribute(name).isLocalized();
        }
        catch(final TypeNotFoundException e)
        {
            LOG.warn(String.format("Could not find data type: '%s' with attribute '%s'", typeCode, name), e);
            return false;
        }
    }


    @Override
    public boolean isValid(final String typeCode, final String name, final Object value)
    {
        final Object filterValue;
        final boolean isFilterLocalized = getSearchStrategy().isLocalized(typeCode, name);
        final boolean isValueLocalized = value instanceof Map;
        if(isFilterLocalized && value == null)
        {
            return true;
        }
        if((isFilterLocalized && !isValueLocalized) || (!isFilterLocalized && isValueLocalized))
        {
            return false;
        }
        if(isFilterLocalized && !((Map)value).isEmpty())
        {
            filterValue = ((Map)value).values().stream().filter(Objects::nonNull).findAny().orElse(null);
        }
        else
        {
            filterValue = value;
        }
        final String fieldType = getSearchStrategy().getFieldType(typeCode, name);
        return StringUtils.isNotEmpty(fieldType)
                        && (filterValue == null || Objects.equals(fieldType, getTypeFacade().getType(filterValue)));
    }


    @Override
    public boolean canHandle(final String searchStrategy)
    {
        return getSearchStrategy().getStrategyName().equals(searchStrategy);
    }


    protected FullTextSearchStrategy getSearchStrategy()
    {
        return searchStrategy;
    }


    public void setSearchStrategy(final FullTextSearchStrategy searchStrategy)
    {
        this.searchStrategy = searchStrategy;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
