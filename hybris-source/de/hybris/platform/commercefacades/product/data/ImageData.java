package de.hybris.platform.commercefacades.product.data;

import java.io.Serializable;

public class ImageData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ImageDataType imageType;
    private String format;
    private String url;
    private String altText;
    private Integer galleryIndex;
    private Integer width;


    public void setImageType(ImageDataType imageType)
    {
        this.imageType = imageType;
    }


    public ImageDataType getImageType()
    {
        return this.imageType;
    }


    public void setFormat(String format)
    {
        this.format = format;
    }


    public String getFormat()
    {
        return this.format;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setAltText(String altText)
    {
        this.altText = altText;
    }


    public String getAltText()
    {
        return this.altText;
    }


    public void setGalleryIndex(Integer galleryIndex)
    {
        this.galleryIndex = galleryIndex;
    }


    public Integer getGalleryIndex()
    {
        return this.galleryIndex;
    }


    public void setWidth(Integer width)
    {
        this.width = width;
    }


    public Integer getWidth()
    {
        return this.width;
    }
}
