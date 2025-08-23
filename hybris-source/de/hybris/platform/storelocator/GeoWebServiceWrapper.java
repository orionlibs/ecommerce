package de.hybris.platform.storelocator;

import de.hybris.platform.storelocator.data.AddressData;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.route.DistanceAndRoute;

public interface GeoWebServiceWrapper
{
    GPS geocodeAddress(Location paramLocation);


    GPS geocodeAddress(AddressData paramAddressData);


    DistanceAndRoute getDistanceAndRoute(Location paramLocation1, Location paramLocation2);


    DistanceAndRoute getDistanceAndRoute(GPS paramGPS, Location paramLocation);


    String formatAddress(Location paramLocation);
}
