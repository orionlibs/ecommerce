package de.hybris.platform.commerceservices.search.pagedata;

import java.io.Serializable;
import java.util.List;

@Deprecated(since = "6.5", forRemoval = true)
public class SearchPageData<RESULT> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<RESULT> results;
    private List<SortData> sorts;
    private PaginationData pagination;


    public void setResults(List<RESULT> results)
    {
        this.results = results;
    }


    public List<RESULT> getResults()
    {
        return this.results;
    }


    public void setSorts(List<SortData> sorts)
    {
        this.sorts = sorts;
    }


    public List<SortData> getSorts()
    {
        return this.sorts;
    }


    public void setPagination(PaginationData pagination)
    {
        this.pagination = pagination;
    }


    public PaginationData getPagination()
    {
        return this.pagination;
    }
}
