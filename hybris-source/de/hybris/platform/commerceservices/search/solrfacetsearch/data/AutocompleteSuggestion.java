package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import java.io.Serializable;

public class AutocompleteSuggestion implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String term;


    public void setTerm(String term)
    {
        this.term = term;
    }


    public String getTerm()
    {
        return this.term;
    }
}
