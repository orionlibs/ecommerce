package de.hybris.platform.commerceservices.search.pagedata;

@Deprecated(since = "6.5", forRemoval = true)
public class PaginationData extends PageableData
{
    private int numberOfPages;
    private long totalNumberOfResults;


    public void setNumberOfPages(int numberOfPages)
    {
        this.numberOfPages = numberOfPages;
    }


    public int getNumberOfPages()
    {
        return this.numberOfPages;
    }


    public void setTotalNumberOfResults(long totalNumberOfResults)
    {
        this.totalNumberOfResults = totalNumberOfResults;
    }


    public long getTotalNumberOfResults()
    {
        return this.totalNumberOfResults;
    }
}
