package de.hybris.platform.commercewebservicescommons.dto.storesession;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Language", description = "Representation of a Language")
public class LanguageWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "isocode", value = "iso code of the language")
    private String isocode;
    @ApiModelProperty(name = "name", value = "name of the language")
    private String name;
    @ApiModelProperty(name = "nativeName", value = "name the language in native form")
    private String nativeName;
    @ApiModelProperty(name = "active", value = "true/false indicator when the language is active")
    private Boolean active;


    public void setIsocode(String isocode)
    {
        this.isocode = isocode;
    }


    public String getIsocode()
    {
        return this.isocode;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setNativeName(String nativeName)
    {
        this.nativeName = nativeName;
    }


    public String getNativeName()
    {
        return this.nativeName;
    }


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
    }
}
