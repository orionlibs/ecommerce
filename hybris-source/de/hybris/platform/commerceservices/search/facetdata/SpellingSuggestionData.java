package de.hybris.platform.commerceservices.search.facetdata;

import java.io.Serializable;

public class SpellingSuggestionData<STATE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String suggestion;
    private STATE query;


    public void setSuggestion(String suggestion)
    {
        this.suggestion = suggestion;
    }


    public String getSuggestion()
    {
        return this.suggestion;
    }


    public void setQuery(STATE query)
    {
        this.query = query;
    }


    public STATE getQuery()
    {
        return this.query;
    }
}
