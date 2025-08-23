package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;

public class CategoryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String url;
    private String description;
    private ImageData image;
    private String parentCategoryName;
    private int sequence;


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


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setImage(ImageData image)
    {
        this.image = image;
    }


    public ImageData getImage()
    {
        return this.image;
    }


    public void setParentCategoryName(String parentCategoryName)
    {
        this.parentCategoryName = parentCategoryName;
    }


    public String getParentCategoryName()
    {
        return this.parentCategoryName;
    }


    public void setSequence(int sequence)
    {
        this.sequence = sequence;
    }


    public int getSequence()
    {
        return this.sequence;
    }
}
