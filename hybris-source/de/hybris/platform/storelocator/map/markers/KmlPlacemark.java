package de.hybris.platform.storelocator.map.markers;

public interface KmlPlacemark extends KmlElement
{
    KmlGps getGPS();


    String getName();


    KmlDescription getDescription();


    KmlStyle getStyle();
}
