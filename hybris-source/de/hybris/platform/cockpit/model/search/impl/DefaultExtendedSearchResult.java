package de.hybris.platform.cockpit.model.search.impl;

import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.ResultObject;
import java.util.List;

public class DefaultExtendedSearchResult extends DefaultSearchResult implements ExtendedSearchResult
{
    private final Query query;


    public DefaultExtendedSearchResult(Query query, List<ResultObject> result, int totalCount)
    {
        super(result, query.getStart(), totalCount);
        this.query = query;
    }


    public Query getQuery()
    {
        return this.query;
    }
}
