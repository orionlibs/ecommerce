package de.hybris.platform.storelocator.impl;

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
import de.hybris.platform.util.Config;
import org.springframework.beans.factory.annotation.Required;

public class GoogleMapsServiceWrapper implements GeoWebServiceWrapper
{
    public static final String GOOGLE_MAPS_URL = "google.maps.url";
    public static final String GOOGLE_GEOCODING_URL = "google.geocoding.url";
    private GoogleMapTools googleMapTools;


    protected GoogleMapTools getMapTools(String url)
    {
        this.googleMapTools.setBaseUrl(url);
        return this.googleMapTools;
    }


    public GPS geocodeAddress(Location address) throws GeoServiceWrapperException
    {
        GoogleMapTools mapTools = getMapTools(Config.getString("google.geocoding.url", null));
        return mapTools.geocodeAddress(address);
    }


    public GPS geocodeAddress(AddressData address) throws GeoServiceWrapperException
    {
        GoogleMapTools mapTools = getMapTools(Config.getString("google.geocoding.url", null));
        return mapTools.geocodeAddress(address);
    }


    public DistanceAndRoute getDistanceAndRoute(Location start, Location destination) throws GeoServiceWrapperException
    {
        GoogleMapTools geocodingModule = getMapTools(Config.getString("google.maps.url", null));
        RouteData routeData = geocodingModule.getDistanceAndRoute(start, destination);
        DefaultRoute defaultRoute = new DefaultRoute(start.getGPS(), destination, routeData.getCoordinates());
        return (DistanceAndRoute)new DefaultDistanceAndRoute(routeData.getDistance(), routeData.getEagleFliesDistance(), (Route)defaultRoute);
    }


    public String formatAddress(Location address) throws GeoServiceWrapperException
    {
        return (new GoogleMapTools()).getGoogleQuery(address.getAddressData());
    }


    public DistanceAndRoute getDistanceAndRoute(GPS start, Location destination) throws GeoServiceWrapperException
    {
        GoogleMapTools geocodingModule = getMapTools(Config.getString("google.maps.url", null));
        RouteData routeData = geocodingModule.getDistanceAndRoute(start, destination.getGPS());
        DefaultRoute defaultRoute = new DefaultRoute(start, destination, routeData.getCoordinates());
        return (DistanceAndRoute)new DefaultDistanceAndRoute(routeData.getDistance(), routeData.getEagleFliesDistance(), (Route)defaultRoute);
    }


    @Required
    public void setGoogleMapTools(GoogleMapTools googleMapTools)
    {
        this.googleMapTools = googleMapTools;
    }
}
