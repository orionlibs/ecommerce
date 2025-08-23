package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Image", description = "Representation of an Image")
public class ImageWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "imageType", value = "Type of the image, can be PRIMARY or GALLERY")
    private ImageWsDTOType imageType;
    @ApiModelProperty(name = "format", value = "Format of the image, can be zoom, product, thumbnail, store, cartIcon, etc.")
    private String format;
    @ApiModelProperty(name = "url", value = "URL address of the image")
    private String url;
    @ApiModelProperty(name = "altText", value = "Tooltip content which is visible while image mouse hovering")
    private String altText;
    @ApiModelProperty(name = "galleryIndex", value = "Index of the image while displayed in gallery")
    private Integer galleryIndex;


    public void setImageType(ImageWsDTOType imageType)
    {
        this.imageType = imageType;
    }


    public ImageWsDTOType getImageType()
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
}
