package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;
import java.util.Collection;

public class VariantValueCategoryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private int sequence;
    private Collection<VariantCategoryData> superCategories;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }


    public int getSequence()
    {
        return this.sequence;
    }


    public void setSuperCategories(Collection<VariantCategoryData> superCategories)
    {
        this.superCategories = superCategories;
    }


    public Collection<VariantCategoryData> getSuperCategories()
    {
        return this.superCategories;
    }
}
