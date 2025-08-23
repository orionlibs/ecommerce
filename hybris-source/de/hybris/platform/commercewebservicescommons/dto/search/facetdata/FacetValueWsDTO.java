package de.hybris.platform.commercewebservicescommons.dto.search.facetdata;

import de.hybris.platform.commercewebservicescommons.dto.search.SearchStateWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "FacetValue", description = "Representation of a Facet Value")
public class FacetValueWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "name", value = "Name of the facet value")
    private String name;
    @ApiModelProperty(name = "count", value = "Count of the facet value")
    private Long count;
    @ApiModelProperty(name = "query", value = "Query of the facet value")
    private SearchStateWsDTO query;
    @ApiModelProperty(name = "selected", value = "Flag stating if facet value is selected")
    private Boolean selected;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setCount(Long count)
    {
        this.count = count;
    }


    public Long getCount()
    {
        return this.count;
    }


    public void setQuery(SearchStateWsDTO query)
    {
        this.query = query;
    }


    public SearchStateWsDTO getQuery()
    {
        return this.query;
    }


    public void setSelected(Boolean selected)
    {
        this.selected = selected;
    }


    public Boolean getSelected()
    {
        return this.selected;
    }
}
