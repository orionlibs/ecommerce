package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import java.util.List;

public class BaseOptionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String variantType;
    private List<VariantOptionData> options;
    private VariantOptionData selected;


    public void setVariantType(String variantType)
    {
        this.variantType = variantType;
    }


    public String getVariantType()
    {
        return this.variantType;
    }


    public void setOptions(List<VariantOptionData> options)
    {
        this.options = options;
    }


    public List<VariantOptionData> getOptions()
    {
        return this.options;
    }


    public void setSelected(VariantOptionData selected)
    {
        this.selected = selected;
    }


    public VariantOptionData getSelected()
    {
        return this.selected;
    }
}
