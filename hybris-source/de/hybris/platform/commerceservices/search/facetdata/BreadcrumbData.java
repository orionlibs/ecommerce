package de.hybris.platform.commerceservices.search.facetdata;

import java.io.Serializable;

public class BreadcrumbData<STATE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String facetCode;
    private String facetName;
    private String facetValueCode;
    private String facetValueName;
    private STATE removeQuery;
    private STATE truncateQuery;


    public void setFacetCode(String facetCode)
    {
        this.facetCode = facetCode;
    }


    public String getFacetCode()
    {
        return this.facetCode;
    }


    public void setFacetName(String facetName)
    {
        this.facetName = facetName;
    }


    public String getFacetName()
    {
        return this.facetName;
    }


    public void setFacetValueCode(String facetValueCode)
    {
        this.facetValueCode = facetValueCode;
    }


    public String getFacetValueCode()
    {
        return this.facetValueCode;
    }


    public void setFacetValueName(String facetValueName)
    {
        this.facetValueName = facetValueName;
    }


    public String getFacetValueName()
    {
        return this.facetValueName;
    }


    public void setRemoveQuery(STATE removeQuery)
    {
        this.removeQuery = removeQuery;
    }


    public STATE getRemoveQuery()
    {
        return this.removeQuery;
    }


    public void setTruncateQuery(STATE truncateQuery)
    {
        this.truncateQuery = truncateQuery;
    }


    public STATE getTruncateQuery()
    {
        return this.truncateQuery;
    }
}
