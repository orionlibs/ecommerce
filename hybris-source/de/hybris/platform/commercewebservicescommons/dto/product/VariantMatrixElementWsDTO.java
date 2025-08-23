package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "VariantMatrixElement", description = "Representation of a Variant Matrix Element")
public class VariantMatrixElementWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "variantValueCategory", value = "Variant value category for variant matrix element")
    private VariantValueCategoryWsDTO variantValueCategory;
    @ApiModelProperty(name = "parentVariantCategory", value = "Parent variant category for variant matrix element")
    private VariantCategoryWsDTO parentVariantCategory;
    @ApiModelProperty(name = "variantOption", value = "Variant option for variant matrix element")
    private VariantOptionWsDTO variantOption;
    @ApiModelProperty(name = "elements", value = "List of elements with the type of variant matrix element")
    private List<VariantMatrixElementWsDTO> elements;
    @ApiModelProperty(name = "isLeaf")
    private Boolean isLeaf;


    public void setVariantValueCategory(VariantValueCategoryWsDTO variantValueCategory)
    {
        this.variantValueCategory = variantValueCategory;
    }


    public VariantValueCategoryWsDTO getVariantValueCategory()
    {
        return this.variantValueCategory;
    }


    public void setParentVariantCategory(VariantCategoryWsDTO parentVariantCategory)
    {
        this.parentVariantCategory = parentVariantCategory;
    }


    public VariantCategoryWsDTO getParentVariantCategory()
    {
        return this.parentVariantCategory;
    }


    public void setVariantOption(VariantOptionWsDTO variantOption)
    {
        this.variantOption = variantOption;
    }


    public VariantOptionWsDTO getVariantOption()
    {
        return this.variantOption;
    }


    public void setElements(List<VariantMatrixElementWsDTO> elements)
    {
        this.elements = elements;
    }


    public List<VariantMatrixElementWsDTO> getElements()
    {
        return this.elements;
    }


    public void setIsLeaf(Boolean isLeaf)
    {
        this.isLeaf = isLeaf;
    }


    public Boolean getIsLeaf()
    {
        return this.isLeaf;
    }
}
