package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;

@ApiModel(value = "VariantValueCategory", description = "Representation of a Variant Value Category")
public class VariantValueCategoryWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "name", value = "Name of the variant value category")
    private String name;
    @ApiModelProperty(name = "sequence", value = "Sequence number of variant value category")
    private Integer sequence;
    @ApiModelProperty(name = "superCategories", value = "Parent category of variant value category")
    private Collection<VariantCategoryWsDTO> superCategories;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setSequence(Integer sequence)
    {
        this.sequence = sequence;
    }


    public Integer getSequence()
    {
        return this.sequence;
    }


    public void setSuperCategories(Collection<VariantCategoryWsDTO> superCategories)
    {
        this.superCategories = superCategories;
    }


    public Collection<VariantCategoryWsDTO> getSuperCategories()
    {
        return this.superCategories;
    }
}
