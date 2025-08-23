package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;

public class VariantCategoryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private Boolean hasImage;
    private int priority;


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


    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    public int getPriority()
    {
        return this.priority;
    }
}
