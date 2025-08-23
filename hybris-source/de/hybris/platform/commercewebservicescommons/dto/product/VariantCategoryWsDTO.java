package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "VariantCategory", description = "Representation of a Variant Category")
public class VariantCategoryWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "name", value = "Variant category name")
    private String name;
    @ApiModelProperty(name = "hasImage", value = "Flag if varian category has image assigned")
    private Boolean hasImage;
    @ApiModelProperty(name = "priority", value = "Priority number of variant category")
    private Integer priority;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setHasImage(Boolean hasImage)
    {
        this.hasImage = hasImage;
    }


    public Boolean getHasImage()
    {
        return this.hasImage;
    }


    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }


    public Integer getPriority()
    {
        return this.priority;
    }
}
