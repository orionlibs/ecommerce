package de.hybris.platform.storelocator.map.markers.impl;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.KmlDocumentException;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.map.markers.KmlDescription;
import de.hybris.platform.storelocator.map.markers.KmlDocument;
import de.hybris.platform.storelocator.map.markers.KmlGps;
import de.hybris.platform.storelocator.map.markers.KmlIconStyle;
import de.hybris.platform.storelocator.map.markers.KmlPlacemark;
import de.hybris.platform.storelocator.map.markers.KmlRoute;
import de.hybris.platform.storelocator.map.markers.KmlStyle;
import de.hybris.platform.storelocator.route.DistanceAndRoute;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultKmlDocument implements KmlDocument
{
    private static final String DEFAULT_BEGINING = "<kml><Document>";
    private static final String DEFAULT_ENDING = "</Document></kml>";
    private String name;
    private String description;
    private final List<KmlPlacemark> placemarks;
    private List<KmlStyle> styles;
    private List<KmlRoute> routes;


    public DefaultKmlDocument(GPS center, List<Location> pois)
    {
        this(center, pois, null);
    }


    public DefaultKmlDocument(GPS center, List<Location> pois, DistanceAndRoute route)
    {
        try
        {
            List<KmlRoute> theRoutes = null;
            List<KmlPlacemark> thePlacemarks = new ArrayList<>();
            if(Objects.nonNull(pois))
            {
                pois.stream().filter(Objects::nonNull).forEach(poi -> {
                    if(Objects.nonNull(poi.getMapIconUrl()))
                    {
                        int styleCount = Objects.nonNull(this.styles) ? this.styles.size() : 0;
                        DefaultIconStyle defaultIconStyle = new DefaultIconStyle("style" + styleCount, poi.getMapIconUrl());
                        addDocumentStyle((KmlIconStyle)defaultIconStyle);
                        thePlacemarks.add(new DefaultKmlPlacemark(poi.getName(), (KmlDescription)new DefaultKmlDescription(poi.getDescription()), (KmlGps)new DefaultKmlGps(poi.getGPS()), (KmlIconStyle)defaultIconStyle));
                    }
                    else
                    {
                        thePlacemarks.add(new DefaultKmlPlacemark(poi.getName(), (KmlDescription)new DefaultKmlDescription(poi.getDescription()), (KmlGps)new DefaultKmlGps(poi.getGPS())));
                    }
                });
            }
            this.placemarks = thePlacemarks;
            if(Objects.nonNull(route) && Objects.nonNull(route.getRoute()))
            {
                if(Objects.isNull(this.styles))
                {
                    this.styles = new ArrayList<>();
                }
                this.styles.add(route.getRoute().getRouteStyle());
                theRoutes = new ArrayList<>();
                theRoutes.add(route.getRoute());
                this.routes = theRoutes;
            }
        }
        catch(UnsupportedOperationException | ClassCastException | IllegalArgumentException e)
        {
            throw new KmlDocumentException(e);
        }
    }


    public DefaultKmlDocument(List<KmlPlacemark> placemarks)
    {
        this.placemarks = placemarks;
    }


    public DefaultKmlDocument(String name, String description, List<KmlPlacemark> placemarks, List<KmlStyle> styles)
    {
        this.name = name;
        this.description = description;
        this.placemarks = placemarks;
        this.styles = styles;
    }


    public List<KmlPlacemark> getPlacemarks()
    {
        return this.placemarks;
    }


    public List<KmlStyle> getStyles()
    {
        return this.styles;
    }


    public String getBeginning()
    {
        return "<kml><Document>";
    }


    public String getElement()
    {
        StringBuilder document = new StringBuilder(getBeginning());
        if(Objects.nonNull(this.styles))
        {
            for(KmlStyle style : this.styles)
            {
                document.append(style.getElement());
            }
        }
        if(Objects.nonNull(this.placemarks))
        {
            for(KmlPlacemark placemark : this.placemarks)
            {
                document.append(placemark.getElement());
            }
        }
        if(Objects.nonNull(this.routes))
        {
            for(KmlRoute route : this.routes)
            {
                document.append(route.getElement());
            }
        }
        document.append(getEnding());
        return document.toString();
    }


    public String getEnding()
    {
        return "</Document></kml>";
    }


    public String getDescription()
    {
        return this.description;
    }


    public String getName()
    {
        return this.name;
    }


    protected void addDocumentStyle(KmlIconStyle iconStyle)
    {
        if(Objects.isNull(this.styles))
        {
            this.styles = new ArrayList<>();
        }
        this.styles.add(iconStyle);
    }
}
