package de.hybris.platform.storelocator.impl;

import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.GeoWebServiceWrapper;
import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.data.RouteData;
import de.hybris.platform.storelocator.exception.GeoServiceWrapperException;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.route.DistanceAndRoute;
import de.hybris.platform.storelocator.route.Route;
import de.hybris.platform.storelocator.route.impl.DefaultDistanceAndRoute;
import de.hybris.platform.storelocator.route.impl.DefaultRoute;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class CommerceMockGeoWebServiceWrapper implements GeoWebServiceWrapper
{
    private BaseStoreService baseStoreService;
    private Map<String, GPS> countryIsoGPSLocationMap;


    public GPS geocodeAddress(Location address)
    {
        if("exception".equals(address.getAddressData().getCity()))
        {
            throw new GeoServiceWrapperException();
        }
        return getCountryIsoGPSLocationMap().containsKey(address.getCountry()) ?
                        getCountryIsoGPSLocationMap().get(address.getCountry()) : getCountryIsoGPSLocationMap().get("DEFAULT");
    }


    public GPS geocodeAddress(AddressData address)
    {
        if("exception".equals(address.getCity()))
        {
            throw new GeoServiceWrapperException();
        }
        return getCountryIsoGPSLocationMap().containsKey(address.getCountryCode()) ?
                        getCountryIsoGPSLocationMap().get(address.getCountryCode()) : getCountryIsoGPSLocationMap().get("DEFAULT");
    }


    public DistanceAndRoute getDistanceAndRoute(Location start, Location destination)
    {
        RouteData routeData = new RouteData();
        routeData.setEagleFliesDistance(0.3D);
        routeData.setDistance(309.0D);
        DefaultRoute defaultRoute = new DefaultRoute(start.getGPS(), destination, routeData.getCoordinates());
        return (DistanceAndRoute)new DefaultDistanceAndRoute(routeData.getDistance(), routeData.getEagleFliesDistance(), (Route)defaultRoute);
    }


    public DistanceAndRoute getDistanceAndRoute(GPS start, Location destination)
    {
        RouteData routeData = new RouteData();
        routeData.setEagleFliesDistance(0.3D);
        routeData.setDistance(10.0D);
        DefaultRoute defaultRoute = new DefaultRoute(start, destination, routeData.getCoordinates());
        return (DistanceAndRoute)new DefaultDistanceAndRoute(routeData.getDistance(), routeData.getEagleFliesDistance(), (Route)defaultRoute);
    }


    public String formatAddress(Location address)
    {
        return "Formatted address";
    }


    protected BaseStoreService getBaseStoreService()
    {
        return this.baseStoreService;
    }


    @Required
    public void setBaseStoreService(BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }


    @Required
    public void setCountryIsoGPSLocationMap(Map<String, GPS> countryIsoGPSLocationMap)
    {
        this.countryIsoGPSLocationMap = countryIsoGPSLocationMap;
    }


    protected Map<String, GPS> getCountryIsoGPSLocationMap()
    {
        return this.countryIsoGPSLocationMap;
    }
}
