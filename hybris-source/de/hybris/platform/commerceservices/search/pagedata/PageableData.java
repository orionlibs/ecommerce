package de.hybris.platform.commerceservices.search.pagedata;

import java.io.Serializable;

@Deprecated(since = "6.5", forRemoval = true)
public class PageableData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int pageSize;
    private int currentPage;
    private String sort;


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


    public void setSort(String sort)
    {
        this.sort = sort;
    }


    public String getSort()
    {
        return this.sort;
    }
}
