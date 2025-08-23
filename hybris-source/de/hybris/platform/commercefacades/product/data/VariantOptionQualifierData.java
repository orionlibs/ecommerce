package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;

public class VariantOptionQualifierData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String qualifier;
    private String name;
    private String value;
    private ImageData image;


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }


    public void setImage(ImageData image)
    {
        this.image = image;
    }


    public ImageData getImage()
    {
        return this.image;
    }
}
