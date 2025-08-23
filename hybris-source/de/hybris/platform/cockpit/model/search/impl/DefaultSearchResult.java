package de.hybris.platform.cockpit.model.search.impl;

import de.hybris.platform.cockpit.model.search.ResultObject;
import de.hybris.platform.cockpit.model.search.SearchResult;
import java.util.ArrayList;
import java.util.List;

public class DefaultSearchResult implements SearchResult
{
    private final int totalCount;
    private final int start;
    private final List<ResultObject> result;


    public DefaultSearchResult(List<ResultObject> result, int start, int totalCount)
    {
        this.result = new ArrayList<>(result);
        this.totalCount = totalCount;
        this.start = start;
    }


    public List<ResultObject> getResult()
    {
        return this.result;
    }


    public int getTotalCount()
    {
        return this.totalCount;
    }


    public int getStart()
    {
        return this.start;
    }
}
