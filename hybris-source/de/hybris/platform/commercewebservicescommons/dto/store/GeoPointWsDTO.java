package de.hybris.platform.commercewebservicescommons.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "GeoPoint", description = "Representation of a GeoPoint")
public class GeoPointWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "latitude", value = "Geopoint latitude")
    private Double latitude;
    @ApiModelProperty(name = "longitude", value = "Geopoint longitude")
    private Double longitude;


    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }


    public Double getLatitude()
    {
        return this.latitude;
    }


    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }


    public Double getLongitude()
    {
        return this.longitude;
    }
}
