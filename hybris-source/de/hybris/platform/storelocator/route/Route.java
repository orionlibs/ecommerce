package de.hybris.platform.storelocator.route;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.map.markers.KmlRoute;

public interface Route extends KmlRoute
{
    String getCoordinates();


    GPS getStart();


    Location getDestination();
}
