package de.hybris.platform.commercewebservicescommons.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "StoreCountList", description = "Representation of a Store Count List")
public class StoreCountListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "countriesAndRegionsStoreCount", value = "List of store counts")
    private List<StoreCountWsDTO> countriesAndRegionsStoreCount;


    public void setCountriesAndRegionsStoreCount(List<StoreCountWsDTO> countriesAndRegionsStoreCount)
    {
        this.countriesAndRegionsStoreCount = countriesAndRegionsStoreCount;
    }


    public List<StoreCountWsDTO> getCountriesAndRegionsStoreCount()
    {
        return this.countriesAndRegionsStoreCount;
    }
}
