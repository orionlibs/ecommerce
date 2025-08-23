package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class NamedQueryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String namedQuery;
    private String params;
    private String sort;
    private String pageSize;
    private String currentPage;
    private Class<?> queryType;


    public void setNamedQuery(String namedQuery)
    {
        this.namedQuery = namedQuery;
    }


    public String getNamedQuery()
    {
        return this.namedQuery;
    }


    public void setParams(String params)
    {
        this.params = params;
    }


    public String getParams()
    {
        return this.params;
    }


    public void setSort(String sort)
    {
        this.sort = sort;
    }


    public String getSort()
    {
        return this.sort;
    }


    public void setPageSize(String pageSize)
    {
        this.pageSize = pageSize;
    }


    public String getPageSize()
    {
        return this.pageSize;
    }


    public void setCurrentPage(String currentPage)
    {
        this.currentPage = currentPage;
    }


    public String getCurrentPage()
    {
        return this.currentPage;
    }


    public void setQueryType(Class<?> queryType)
    {
        this.queryType = queryType;
    }


    public Class<?> getQueryType()
    {
        return this.queryType;
    }
}
