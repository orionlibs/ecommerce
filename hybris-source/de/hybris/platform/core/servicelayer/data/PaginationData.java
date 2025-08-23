package de.hybris.platform.core.servicelayer.data;

import java.io.Serializable;

public class PaginationData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int pageSize;
    private int currentPage;
    private int numberOfPages;
    private long totalNumberOfResults;
    private boolean needsTotal;
    private Boolean hasNext;
    private Boolean hasPrevious;


    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public void setCurrentPage(int currentPage)
    {
        this.currentPage = currentPage;
    }


    public int getCurrentPage()
    {
        return this.currentPage;
    }


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


    public void setNeedsTotal(boolean needsTotal)
    {
        this.needsTotal = needsTotal;
    }


    public boolean isNeedsTotal()
    {
        return this.needsTotal;
    }


    public void setHasNext(Boolean hasNext)
    {
        this.hasNext = hasNext;
    }


    public Boolean getHasNext()
    {
        return this.hasNext;
    }


    public void setHasPrevious(Boolean hasPrevious)
    {
        this.hasPrevious = hasPrevious;
    }


    public Boolean getHasPrevious()
    {
        return this.hasPrevious;
    }
}
