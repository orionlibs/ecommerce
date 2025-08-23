/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.search.impl;

import com.google.common.base.MoreObjects;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.search.AutosuggestionSupport;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacade;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.search.data.AutosuggestionQueryData;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of FieldSearchFacade. Delegates to a matching
 * {@link com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacadeStrategy}. Method
 * {@link #search(SearchQueryData)} returns an empty {@link PageableList} if no strategy has been found.
 */
public class DefaultFieldSearchFacade implements FieldSearchFacade<Object>, AutosuggestionSupport
{
    private FieldSearchFacadeStrategyRegistry fieldSearchFacadeStrategyRegistry;


    @Override
    public Pageable<Object> search(final SearchQueryData searchQueryData)
    {
        Validate.notNull("Passed parameters may not be null!", searchQueryData);
        return searchInternal(searchQueryData, new DefaultContext());
    }


    @Override
    public Pageable<Object> search(final SearchQueryData searchQueryData, final Context context)
    {
        Validate.notNull("Passed parameters may not be null!", searchQueryData, context);
        return searchInternal(searchQueryData, context);
    }


    protected Pageable<Object> searchInternal(final SearchQueryData searchQueryData, final Context context)
    {
        context.addAttribute(FieldSearchFacadeStrategy.CONTEXT_ORIGINAL_QUERY, searchQueryData);
        final FieldSearchFacadeStrategy fieldSearchFacadeStrategy = this.fieldSearchFacadeStrategyRegistry
                        .getStrategy(searchQueryData.getSearchType(), context);
        if(fieldSearchFacadeStrategy == null)
        {
            return new PageableList<>(Collections.emptyList(), 10);
        }
        else
        {
            return fieldSearchFacadeStrategy.search(searchQueryData, context);
        }
    }


    @Override
    public boolean isSortable(final DataType type, final String attributeQualifier, Context context)
    {
        context = MoreObjects.firstNonNull(context, new DefaultContext());
        final FieldSearchFacadeStrategy fieldSearchFacadeStrategy = this.fieldSearchFacadeStrategyRegistry
                        .getStrategy(type.getCode(), context);
        return fieldSearchFacadeStrategy.isSortable(type, attributeQualifier, context);
    }


    @Override
    public Map<String, Collection<String>> getAutosuggestionsForQuery(final AutosuggestionQueryData queryData)
    {
        return getAutosuggestionsForQuery(queryData, new DefaultContext());
    }


    @Override
    public Map<String, Collection<String>> getAutosuggestionsForQuery(final AutosuggestionQueryData queryData, final Context context)
    {
        Validate.notNull("Passed parameters may not be null!", queryData);
        final FieldSearchFacadeStrategy fieldSearchFacadeStrategy = this.fieldSearchFacadeStrategyRegistry
                        .getStrategy(queryData.getSearchType(), context);
        if(fieldSearchFacadeStrategy instanceof AutosuggestionSupport)
        {
            return ((AutosuggestionSupport)fieldSearchFacadeStrategy).getAutosuggestionsForQuery(queryData);
        }
        else
        {
            return Collections.emptyMap();
        }
    }


    @Required
    public void setFieldSearchFacadeStrategyRegistry(final FieldSearchFacadeStrategyRegistry fieldSearchFacadeStrategyRegistry)
    {
        this.fieldSearchFacadeStrategyRegistry = fieldSearchFacadeStrategyRegistry;
    }
}
