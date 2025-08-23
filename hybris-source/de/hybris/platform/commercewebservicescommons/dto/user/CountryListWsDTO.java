package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "CountryList", description = "List of countries")
public class CountryListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "countries", value = "This is the list of Country fields that should be returned in the response body")
    private List<CountryWsDTO> countries;


    public void setCountries(List<CountryWsDTO> countries)
    {
        this.countries = countries;
    }


    public List<CountryWsDTO> getCountries()
    {
        return this.countries;
    }
}
