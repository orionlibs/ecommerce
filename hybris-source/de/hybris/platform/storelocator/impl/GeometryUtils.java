package de.hybris.platform.storelocator.impl;

import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.constants.GeolocationMaths;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import java.util.ArrayList;
import java.util.List;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

public class GeometryUtils
{
    public static final Ellipsoid KRASSOWSKI = Ellipsoid.fromAAndInverseF(6378245.0D, 298.3D);
    public static final double LAT_DEGREE_DIST = 111.11D;
    public static final double APP_EARTH_RADIUS = 6366.38D;


    public static double getElipticalDistanceKM(GPS gpsFrom, GPS gpsTo)
    {
        Ellipsoid reference;
        GeodeticCalculator geoCalc = new GeodeticCalculator();
        if(isPointInEurope(gpsFrom) || isPointInEurope(gpsTo))
        {
            reference = KRASSOWSKI;
        }
        else
        {
            reference = Ellipsoid.WGS84;
        }
        GeodeticCurve geoCurve = geoCalc.calculateGeodeticCurve(reference, igps2GlobalCoordinates(gpsFrom),
                        igps2GlobalCoordinates(gpsTo));
        return geoCurve.getEllipsoidalDistance() / 1000.0D;
    }


    protected static GlobalCoordinates igps2GlobalCoordinates(GPS igps)
    {
        return new GlobalCoordinates(igps.getDecimalLatitude(), igps.getDecimalLongitude());
    }


    protected static double getCircleOfLatitudeLength(double latitude)
    {
        double rads = Math.abs(latitude) * Math.PI / 180.0D;
        return 40001.145275921976D * Math.cos(rads);
    }


    public static List<GPS> getSquareOfTolerance(GPS center, double radius)
    {
        if(center == null)
        {
            throw new GeoLocatorException("Center cannot be null");
        }
        if(radius <= 0.0D)
        {
            throw new GeoLocatorException("Radius must be a positive value");
        }
        double deltaLongitude = radius / getCircleOfLatitudeLength(center.getDecimalLatitude()) / 360.0D;
        double deltaLatitude = radius / 111.11D;
        double lat1 = fixOverlappedLatitude(center.getDecimalLatitude() - deltaLatitude);
        double lon1 = fixOverlappedLongitude(center.getDecimalLongitude() - deltaLongitude);
        double lat2 = fixOverlappedLatitude(center.getDecimalLatitude() + deltaLatitude);
        double lon2 = fixOverlappedLongitude(center.getDecimalLongitude() + deltaLongitude);
        List<GPS> corners = new ArrayList<>(2);
        DefaultGPS defaultGPS = new DefaultGPS();
        corners.add(defaultGPS.create(lat1, lon1));
        corners.add(defaultGPS.create(lat2, lon2));
        return corners;
    }


    protected static double fixOverlappedLongitude(double longitude)
    {
        if(longitude < -180.0D)
        {
            return 360.0D + longitude;
        }
        if(longitude > 180.0D)
        {
            return -360.0D + longitude;
        }
        return longitude;
    }


    protected static double fixOverlappedLatitude(double latitude)
    {
        if(latitude > 90.0D)
        {
            return 90.0D;
        }
        if(latitude < -90.0D)
        {
            return -90.0D;
        }
        return latitude;
    }


    public static boolean isPointInEurope(GPS point)
    {
        try
        {
            double northernmostPoint = GeolocationMaths.dms2Decimal(new int[] {81, 48, 24});
            double southernmostPoint = GeolocationMaths.dms2Decimal(new int[] {34, 48, 2});
            double westernmostPoint = GeolocationMaths.dms2Decimal(new int[] {-24, 32, 3});
            double easternmostPoint = GeolocationMaths.dms2Decimal(new int[] {69, 2, 0});
            return (northernmostPoint > point.getDecimalLatitude() && southernmostPoint < point.getDecimalLatitude() && westernmostPoint < point
                            .getDecimalLongitude() && easternmostPoint > point.getDecimalLongitude());
        }
        catch(GeoLocatorException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }
}
