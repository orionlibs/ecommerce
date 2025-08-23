package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Country", description = "Response body fields which will be returned while fetching the list of countries. The DTO is in XML or .json format")
public class CountryWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "isocode", value = "Country code in iso format")
    private String isocode;
    @ApiModelProperty(name = "name", value = "Name of the country")
    private String name;


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
}
