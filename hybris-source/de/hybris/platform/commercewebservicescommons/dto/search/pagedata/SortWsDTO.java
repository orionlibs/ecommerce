package de.hybris.platform.commercewebservicescommons.dto.search.pagedata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Sort", description = "Representation a Sort option")
@Deprecated(since = "6.5", forRemoval = true)
public class SortWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of Sort")
    private String code;
    @ApiModelProperty(name = "name", value = "Name of Sort")
    private String name;
    @ApiModelProperty(name = "selected", value = "Flag stating when Sort is selected")
    private Boolean selected;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
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
