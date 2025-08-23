package de.hybris.platform.solrfacetsearch.search.impl;

import de.hybris.platform.solrfacetsearch.search.SearchResultGroup;
import de.hybris.platform.solrfacetsearch.search.SearchResultGroupCommand;
import java.util.List;

public class SolrSearchResultGroupCommand implements SearchResultGroupCommand
{
    private static final long serialVersionUID = 1L;
    private String name;
    private long numberOfMatches;
    private long numberOfGroups;
    private List<SearchResultGroup> groups;


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public long getNumberOfMatches()
    {
        return this.numberOfMatches;
    }


    public void setNumberOfMatches(long numberOfMatches)
    {
        this.numberOfMatches = numberOfMatches;
    }


    public long getNumberOfGroups()
    {
        return this.numberOfGroups;
    }


    public void setNumberOfGroups(long numberOfGroups)
    {
        this.numberOfGroups = numberOfGroups;
    }


    public List<SearchResultGroup> getGroups()
    {
        return this.groups;
    }


    public void setGroups(List<SearchResultGroup> groups)
    {
        this.groups = groups;
    }
}
