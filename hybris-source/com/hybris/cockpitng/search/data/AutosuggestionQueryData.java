/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.search.data;

/**
 * Represents autosuggestions query
 */
public class AutosuggestionQueryData
{
    private final String searchType;
    private final String queryText;


    public AutosuggestionQueryData(final String searchType, final String queryText)
    {
        this.searchType = searchType;
        this.queryText = queryText;
    }


    public String getSearchType()
    {
        return searchType;
    }


    public String getQueryText()
    {
        return queryText;
    }
}
