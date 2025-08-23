package de.hybris.platform.commercefacades.quote.data;

import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import java.io.Serializable;
import java.util.List;

public class QuoteListData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<QuoteData> quotes;
    private List<SortData> sorts;
    private PaginationData pagination;


    public void setQuotes(List<QuoteData> quotes)
    {
        this.quotes = quotes;
    }


    public List<QuoteData> getQuotes()
    {
        return this.quotes;
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
