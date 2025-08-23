package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.search.Document;
import de.hybris.platform.solrfacetsearch.search.SearchResultGroup;
import java.util.List;

public class SolrSearchResultGroup implements SearchResultGroup
{
    private static final long serialVersionUID = 1L;
    private long numberOfResults;
    private String groupValue;
    private List<Document> documents;


    public long getNumberOfResults()
    {
        return this.numberOfResults;
    }


    public void setNumberOfResults(long numberOfResults)
    {
        this.numberOfResults = numberOfResults;
    }


    public String getGroupValue()
    {
        return this.groupValue;
    }


    public void setGroupValue(String groupValue)
    {
        this.groupValue = groupValue;
    }


    public List<Document> getDocuments()
    {
        return this.documents;
    }


    public void setDocuments(List<Document> documents)
    {
        this.documents = documents;
    }
}
