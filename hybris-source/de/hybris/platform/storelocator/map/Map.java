package de.hybris.platform.storelocator.map;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.map.markers.KmlDocument;
import de.hybris.platform.storelocator.map.utils.MapBounds;
import de.hybris.platform.storelocator.route.DistanceAndRoute;
import java.util.List;

public interface Map
{
    double getRadius();


    List<Location> getPointsOfInterest();


    String getTitle();


    KmlDocument getKml();


    GPS getGps();


    DistanceAndRoute getDistanceAndRoute();


    MapBounds getMapBounds();
}
