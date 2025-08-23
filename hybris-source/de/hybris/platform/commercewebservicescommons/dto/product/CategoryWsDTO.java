package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Category", description = "Representation of a Category")
public class CategoryWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of the category")
    private String code;
    @ApiModelProperty(name = "name", value = "Name of the category")
    private String name;
    @ApiModelProperty(name = "url", value = "URL of the category")
    private String url;
    @ApiModelProperty(name = "image", value = "Category image")
    private ImageWsDTO image;


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


    public void setImage(ImageWsDTO image)
    {
        this.image = image;
    }


    public ImageWsDTO getImage()
    {
        return this.image;
    }
}
