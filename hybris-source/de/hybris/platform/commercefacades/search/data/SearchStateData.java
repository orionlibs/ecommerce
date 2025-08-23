package de.hybris.platform.commercefacades.search.data;

import java.io.Serializable;

public class SearchStateData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String url;
    private SearchQueryData query;


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setQuery(SearchQueryData query)
    {
        this.query = query;
    }


    public SearchQueryData getQuery()
    {
        return this.query;
    }
}
