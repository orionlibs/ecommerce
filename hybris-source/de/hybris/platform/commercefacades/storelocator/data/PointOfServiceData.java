package de.hybris.platform.commercefacades.storelocator.data;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class PointOfServiceData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private String displayName;
    private String url;
    private String description;
    private OpeningScheduleData openingHours;
    private String storeContent;
    private Map<String, String> features;
    private GeoPoint geoPoint;
    private String formattedDistance;
    private Double distanceKm;
    private ImageData mapIcon;
    private AddressData address;
    private Collection<ImageData> storeImages;


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


    public void setOpeningHours(OpeningScheduleData openingHours)
    {
        this.openingHours = openingHours;
    }


    public OpeningScheduleData getOpeningHours()
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


    public void setGeoPoint(GeoPoint geoPoint)
    {
        this.geoPoint = geoPoint;
    }


    public GeoPoint getGeoPoint()
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


    public void setMapIcon(ImageData mapIcon)
    {
        this.mapIcon = mapIcon;
    }


    public ImageData getMapIcon()
    {
        return this.mapIcon;
    }


    public void setAddress(AddressData address)
    {
        this.address = address;
    }


    public AddressData getAddress()
    {
        return this.address;
    }


    public void setStoreImages(Collection<ImageData> storeImages)
    {
        this.storeImages = storeImages;
    }


    public Collection<ImageData> getStoreImages()
    {
        return this.storeImages;
    }
}
