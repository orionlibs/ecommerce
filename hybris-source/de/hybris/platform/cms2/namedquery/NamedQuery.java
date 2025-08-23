package de.hybris.platform.cms2.namedquery;

import java.util.List;
import java.util.Map;

public class NamedQuery
{
    private String queryName;
    private List<Sort> sort;
    private Integer currentPage;
    private Integer pageSize;
    private Map<String, ? extends Object> parameters;


    public void setQueryName(String queryName)
    {
        this.queryName = queryName;
    }


    public NamedQuery withQueryName(String queryName)
    {
        this.queryName = queryName;
        return this;
    }


    public String getQueryName()
    {
        return this.queryName;
    }


    public void setSort(List<Sort> sort)
    {
        this.sort = sort;
    }


    public NamedQuery withSort(List<Sort> sort)
    {
        this.sort = sort;
        return this;
    }


    public List<Sort> getSort()
    {
        return this.sort;
    }


    public void setCurrentPage(Integer currentPage)
    {
        this.currentPage = currentPage;
    }


    public NamedQuery withCurrentPage(Integer currentPage)
    {
        this.currentPage = currentPage;
        return this;
    }


    public Integer getCurrentPage()
    {
        return this.currentPage;
    }


    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }


    public NamedQuery withPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
        return this;
    }


    public Integer getPageSize()
    {
        return this.pageSize;
    }


    public void setParameters(Map<String, ? extends Object> parameters)
    {
        this.parameters = parameters;
    }


    public NamedQuery withParameters(Map<String, ? extends Object> parameters)
    {
        this.parameters = parameters;
        return this;
    }


    public Map<String, ? extends Object> getParameters()
    {
        return this.parameters;
    }
}
