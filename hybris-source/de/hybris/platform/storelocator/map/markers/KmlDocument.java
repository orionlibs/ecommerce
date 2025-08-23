package de.hybris.platform.storelocator.map.markers;

import java.util.List;

public interface KmlDocument extends KmlElement
{
    List<KmlPlacemark> getPlacemarks();


    List<KmlStyle> getStyles();


    String getName();


    String getDescription();
}
