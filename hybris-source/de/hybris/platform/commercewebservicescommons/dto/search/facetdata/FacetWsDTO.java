package de.hybris.platform.commercewebservicescommons.dto.search.facetdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "Facet", description = "Representation of a Facet")
public class FacetWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "name", value = "Name of the facet")
    private String name;
    @ApiModelProperty(name = "priority", value = "Priority value of the facet")
    private Integer priority;
    @ApiModelProperty(name = "category", value = "Flag stating if facet is category facet")
    private Boolean category;
    @ApiModelProperty(name = "multiSelect", value = "Flag stating if facet is multiSelect")
    private Boolean multiSelect;
    @ApiModelProperty(name = "visible", value = "Flag stating if facet is visible")
    private Boolean visible;
    @ApiModelProperty(name = "topValues", value = "List of top facet values")
    private List<FacetValueWsDTO> topValues;
    @ApiModelProperty(name = "values", value = "List of all facet values")
    private List<FacetValueWsDTO> values;
    @ApiModelProperty(name = "code")
    private String code;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }


    public Integer getPriority()
    {
        return this.priority;
    }


    public void setCategory(Boolean category)
    {
        this.category = category;
    }


    public Boolean getCategory()
    {
        return this.category;
    }


    public void setMultiSelect(Boolean multiSelect)
    {
        this.multiSelect = multiSelect;
    }


    public Boolean getMultiSelect()
    {
        return this.multiSelect;
    }


    public void setVisible(Boolean visible)
    {
        this.visible = visible;
    }


    public Boolean getVisible()
    {
        return this.visible;
    }


    public void setTopValues(List<FacetValueWsDTO> topValues)
    {
        this.topValues = topValues;
    }


    public List<FacetValueWsDTO> getTopValues()
    {
        return this.topValues;
    }


    public void setValues(List<FacetValueWsDTO> values)
    {
        this.values = values;
    }


    public List<FacetValueWsDTO> getValues()
    {
        return this.values;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }
}
