package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Region", description = "Response body fields which will be returned while fetching the list of country's regions.")
public class RegionWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "isocode", value = "Country and Region code in iso format")
    private String isocode;
    @ApiModelProperty(name = "isocodeShort", value = "Region code in short iso form")
    private String isocodeShort;
    @ApiModelProperty(name = "countryIso", value = "Country code in iso format")
    private String countryIso;
    @ApiModelProperty(name = "name", value = "Name of the region")
    private String name;


    public void setIsocode(String isocode)
    {
        this.isocode = isocode;
    }


    public String getIsocode()
    {
        return this.isocode;
    }


    public void setIsocodeShort(String isocodeShort)
    {
        this.isocodeShort = isocodeShort;
    }


    public String getIsocodeShort()
    {
        return this.isocodeShort;
    }


    public void setCountryIso(String countryIso)
    {
        this.countryIso = countryIso;
    }


    public String getCountryIso()
    {
        return this.countryIso;
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
