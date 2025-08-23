package de.hybris.platform.commercewebservicescommons.dto.search.facetdata;

import de.hybris.platform.commercewebservicescommons.dto.search.SearchStateWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Breadcrumb", description = "Representation of a Breadcrumb")
public class BreadcrumbWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "facetCode", value = "Code of the facet")
    private String facetCode;
    @ApiModelProperty(name = "facetName", value = "Name of the facet")
    private String facetName;
    @ApiModelProperty(name = "facetValueCode", value = "Value code of the facet")
    private String facetValueCode;
    @ApiModelProperty(name = "facetValueName", value = "Value name of the facet")
    private String facetValueName;
    @ApiModelProperty(name = "removeQuery", value = "Remove query")
    private SearchStateWsDTO removeQuery;
    @ApiModelProperty(name = "truncateQuery", value = "Truncate query")
    private SearchStateWsDTO truncateQuery;


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


    public void setRemoveQuery(SearchStateWsDTO removeQuery)
    {
        this.removeQuery = removeQuery;
    }


    public SearchStateWsDTO getRemoveQuery()
    {
        return this.removeQuery;
    }


    public void setTruncateQuery(SearchStateWsDTO truncateQuery)
    {
        this.truncateQuery = truncateQuery;
    }


    public SearchStateWsDTO getTruncateQuery()
    {
        return this.truncateQuery;
    }
}
