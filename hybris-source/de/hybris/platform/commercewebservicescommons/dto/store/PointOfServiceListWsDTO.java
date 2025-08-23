package de.hybris.platform.commercewebservicescommons.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "PointOfServiceList", description = "Representation of a Point of Service List")
public class PointOfServiceListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "pointOfServices", value = "List of points of service")
    private List<PointOfServiceWsDTO> pointOfServices;


    public void setPointOfServices(List<PointOfServiceWsDTO> pointOfServices)
    {
        this.pointOfServices = pointOfServices;
    }


    public List<PointOfServiceWsDTO> getPointOfServices()
    {
        return this.pointOfServices;
    }
}
