package de.hybris.platform.storelocator.location.impl;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.exception.LocationMapServiceException;
import de.hybris.platform.storelocator.exception.LocationServiceException;
import de.hybris.platform.storelocator.exception.MapServiceException;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.LocationMapService;
import de.hybris.platform.storelocator.location.LocationService;
import de.hybris.platform.storelocator.map.Map;
import de.hybris.platform.storelocator.map.MapService;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultLocationMapService implements LocationMapService
{
    private LocationService locationService;
    private GeoWebServiceWrapper geoServiceWrapper;
    private MapService mapService;
    private double radiusStep = 50.0D;
    private double radiusMax = 500.0D;
    private static final String CANNOT_TRANSLATE_ADDRESS_MESSAGE = "Cannot translate AddressData to IGPS.";


    public Map getMapOfLocationsForPostcode(String postalCode, String countryCode, int limitLocationsCount, BaseStoreModel baseStore)
    {
        validateInputData(postalCode, "Postal code cannot be empty!");
        validateInputData(countryCode, "Country code cannot be empty!");
        try
        {
            GPS gps = calculateGPS(null, postalCode, countryCode);
            return getMapOfLocations(gps, limitLocationsCount, baseStore);
        }
        catch(GeoServiceWrapperException e)
        {
            throw new LocationMapServiceException("Cannot translate AddressData to IGPS.", e);
        }
        catch(MapServiceException e)
        {
            throw new LocationMapServiceException("Cannot create new Map.", e);
        }
    }


    public Map getMapOfLocationsForTown(String town, int limitLocationsCount, BaseStoreModel baseStore)
    {
        validateInputData(town, "Town name cannot be empty!");
        try
        {
            GPS gps = calculateGPS(town, null, null);
            return getMapOfLocations(gps, limitLocationsCount, baseStore);
        }
        catch(GeoServiceWrapperException e)
        {
            throw new LocationMapServiceException("Cannot translate AddressData to IGPS.", e);
        }
        catch(MapServiceException e)
        {
            throw new LocationMapServiceException("Cannot create new Map.", e);
        }
    }


    public Map getMapOfLocations(String searchTerm, String countryCode, int limitLocationsCount, BaseStoreModel baseStore)
    {
        Map result = getMapOfLocationsForTown(searchTerm, limitLocationsCount, baseStore);
        if(CollectionUtils.isEmpty(result.getPointsOfInterest()))
        {
            result = getMapOfLocationsForPostcode(searchTerm, countryCode, limitLocationsCount, baseStore);
        }
        return result;
    }


    public Map getMapOfLocations(GPS gps, int limitLocationsCount, BaseStoreModel baseStore)
    {
        List<Location> locations;
        double distance = 0.0D;
        try
        {
            do
            {
                distance += getRadiusStep();
                locations = getLocationService().getSortedLocationsNearby(gps, distance, baseStore);
            }
            while(locations.size() < limitLocationsCount && distance < getRadiusMax());
        }
        catch(LocationServiceException e)
        {
            throw new LocationMapServiceException("Cannot translate AddressData to IGPS.", e);
        }
        if(limitLocationsCount < locations.size())
        {
            locations = locations.subList(0, limitLocationsCount);
        }
        return getMapService().getMap(gps, distance, locations, "");
    }


    protected void validateInputData(Object input, String message)
    {
        ServicesUtil.validateParameterNotNull(input, message);
    }


    protected void validateInputData(String input, String message)
    {
        if(StringUtils.isEmpty(input))
        {
            throw new IllegalArgumentException(message);
        }
    }


    protected GPS calculateGPS(String town, String postalCode, String countryCode)
    {
        AddressData addressData = new AddressData();
        addressData.setCity(town);
        addressData.setCountryCode(countryCode);
        addressData.setZip(postalCode);
        return getGeoServiceWrapper().geocodeAddress(addressData);
    }


    protected GeoWebServiceWrapper getGeoServiceWrapper()
    {
        return this.geoServiceWrapper;
    }


    @Required
    public void setGeoServiceWrapper(GeoWebServiceWrapper geoServiceWrapper)
    {
        this.geoServiceWrapper = geoServiceWrapper;
    }


    public double getRadiusStep()
    {
        return this.radiusStep;
    }


    public void setRadiusStep(double radiusStep)
    {
        this.radiusStep = radiusStep;
    }


    public double getRadiusMax()
    {
        return this.radiusMax;
    }


    public void setRadiusMax(double radiusMax)
    {
        this.radiusMax = radiusMax;
    }


    public MapService getMapService()
    {
        return this.mapService;
    }


    @Required
    public void setMapService(MapService mapService)
    {
        this.mapService = mapService;
    }


    @Required
    public void setLocationService(LocationService distanceAwareLocationService)
    {
        this.locationService = distanceAwareLocationService;
    }


    public LocationService getLocationService()
    {
        return this.locationService;
    }
}
