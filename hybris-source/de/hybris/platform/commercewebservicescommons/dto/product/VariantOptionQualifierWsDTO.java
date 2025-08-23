package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "VariantOptionQualifier", description = "Representation of a Variant Option Qualifier")
public class VariantOptionQualifierWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "qualifier", value = "Qualifier")
    private String qualifier;
    @ApiModelProperty(name = "name", value = "Name of variant option qualifier")
    private String name;
    @ApiModelProperty(name = "value", value = "Value of variant option qualifier")
    private String value;
    @ApiModelProperty(name = "image", value = "Image associated with variant option qualifier")
    private ImageWsDTO image;


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


    public void setImage(ImageWsDTO image)
    {
        this.image = image;
    }


    public ImageWsDTO getImage()
    {
        return this.image;
    }
}
