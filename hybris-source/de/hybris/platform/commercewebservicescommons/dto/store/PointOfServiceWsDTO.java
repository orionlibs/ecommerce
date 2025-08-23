package de.hybris.platform.commercewebservicescommons.dto.store;

import de.hybris.platform.commercewebservicescommons.dto.product.ImageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@ApiModel(value = "PointOfService", description = "Representation of a Point of service")
public class PointOfServiceWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "name", value = "Name of the point of service")
    private String name;
    @ApiModelProperty(name = "displayName", value = "Display name of the point of service")
    private String displayName;
    @ApiModelProperty(name = "url", value = "Url address of the point of service")
    private String url;
    @ApiModelProperty(name = "description", value = "Description of the point of service")
    private String description;
    @ApiModelProperty(name = "openingHours", value = "Opening hours of point of service")
    private OpeningScheduleWsDTO openingHours;
    @ApiModelProperty(name = "storeContent", value = "Store content of given point of service")
    private String storeContent;
    @ApiModelProperty(name = "features", value = "List of features for a given point of service")
    private Map<String, String> features;
    @ApiModelProperty(name = "geoPoint", value = "Geopoint localization info about point of service")
    private GeoPointWsDTO geoPoint;
    @ApiModelProperty(name = "formattedDistance", value = "Distance to the point of service as text value")
    private String formattedDistance;
    @ApiModelProperty(name = "distanceKm", value = "Distance to the point of service as number value")
    private Double distanceKm;
    @ApiModelProperty(name = "mapIcon", value = "Image associated with the point of service")
    private ImageWsDTO mapIcon;
    @ApiModelProperty(name = "address", value = "Address information of point of service")
    private AddressWsDTO address;
    @ApiModelProperty(name = "storeImages", value = "Collection of images associated with a point of service")
    private Collection<ImageWsDTO> storeImages;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    public String getDisplayName()
    {
        return this.displayName;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setOpeningHours(OpeningScheduleWsDTO openingHours)
    {
        this.openingHours = openingHours;
    }


    public OpeningScheduleWsDTO getOpeningHours()
    {
        return this.openingHours;
    }


    public void setStoreContent(String storeContent)
    {
        this.storeContent = storeContent;
    }


    public String getStoreContent()
    {
        return this.storeContent;
    }


    public void setFeatures(Map<String, String> features)
    {
        this.features = features;
    }


    public Map<String, String> getFeatures()
    {
        return this.features;
    }


    public void setGeoPoint(GeoPointWsDTO geoPoint)
    {
        this.geoPoint = geoPoint;
    }


    public GeoPointWsDTO getGeoPoint()
    {
        return this.geoPoint;
    }


    public void setFormattedDistance(String formattedDistance)
    {
        this.formattedDistance = formattedDistance;
    }


    public String getFormattedDistance()
    {
        return this.formattedDistance;
    }


    public void setDistanceKm(Double distanceKm)
    {
        this.distanceKm = distanceKm;
    }


    public Double getDistanceKm()
    {
        return this.distanceKm;
    }


    public void setMapIcon(ImageWsDTO mapIcon)
    {
        this.mapIcon = mapIcon;
    }


    public ImageWsDTO getMapIcon()
    {
        return this.mapIcon;
    }


    public void setAddress(AddressWsDTO address)
    {
        this.address = address;
    }


    public AddressWsDTO getAddress()
    {
        return this.address;
    }


    public void setStoreImages(Collection<ImageWsDTO> storeImages)
    {
        this.storeImages = storeImages;
    }


    public Collection<ImageWsDTO> getStoreImages()
    {
        return this.storeImages;
    }
}
