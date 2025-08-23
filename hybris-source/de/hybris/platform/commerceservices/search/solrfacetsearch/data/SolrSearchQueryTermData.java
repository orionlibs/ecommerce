package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import java.io.Serializable;

public class SolrSearchQueryTermData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String key;
    private String value;


    public void setKey(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return this.key;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }
}
