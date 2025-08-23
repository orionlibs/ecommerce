package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import java.util.List;

public class VariantMatrixElementData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private VariantValueCategoryData variantValueCategory;
    private VariantCategoryData parentVariantCategory;
    private VariantOptionData variantOption;
    private List<VariantMatrixElementData> elements;
    private Boolean isLeaf;


    public void setVariantValueCategory(VariantValueCategoryData variantValueCategory)
    {
        this.variantValueCategory = variantValueCategory;
    }


    public VariantValueCategoryData getVariantValueCategory()
    {
        return this.variantValueCategory;
    }


    public void setParentVariantCategory(VariantCategoryData parentVariantCategory)
    {
        this.parentVariantCategory = parentVariantCategory;
    }


    public VariantCategoryData getParentVariantCategory()
    {
        return this.parentVariantCategory;
    }


    public void setVariantOption(VariantOptionData variantOption)
    {
        this.variantOption = variantOption;
    }


    public VariantOptionData getVariantOption()
    {
        return this.variantOption;
    }


    public void setElements(List<VariantMatrixElementData> elements)
    {
        this.elements = elements;
    }


    public List<VariantMatrixElementData> getElements()
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
