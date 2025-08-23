package de.hybris.platform.commercewebservicescommons.dto.basesite;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "BaseSiteList", description = "Representation of a Base Site List")
public class BaseSiteListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "baseSites", value = "List of basesites")
    private List<BaseSiteWsDTO> baseSites;


    public void setBaseSites(List<BaseSiteWsDTO> baseSites)
    {
        this.baseSites = baseSites;
    }


    public List<BaseSiteWsDTO> getBaseSites()
    {
        return this.baseSites;
    }
}
