package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "RegionList", description = "List of Regions")
public class RegionListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "regions", value = "This is the list of Region fields that should be returned in the response body")
    private List<RegionWsDTO> regions;


    public void setRegions(List<RegionWsDTO> regions)
    {
        this.regions = regions;
    }


    public List<RegionWsDTO> getRegions()
    {
        return this.regions;
    }
}
