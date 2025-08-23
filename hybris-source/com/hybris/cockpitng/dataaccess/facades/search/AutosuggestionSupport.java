/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.search;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.search.data.AutosuggestionQueryData;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Interface for search autosuggestions.
 */
public interface AutosuggestionSupport
{
    /**
     * Returns autosuggestions for given type and query text.
     *
     * @param queryData autosuggestion data with type and query text.
     * @return auto suggestions.
     */
    Map<String, Collection<String>> getAutosuggestionsForQuery(final AutosuggestionQueryData queryData);


    /**
     * Returns autosuggestions for given type and query text.
     *
     * @param queryData autosuggestion data with type and query text.
     * @param context context of search operation {@link Context}
     * @return auto suggestions.
     */
    default Map<String, Collection<String>> getAutosuggestionsForQuery(final AutosuggestionQueryData queryData, final Context context)
    {
        return Collections.emptyMap();
    }
}
