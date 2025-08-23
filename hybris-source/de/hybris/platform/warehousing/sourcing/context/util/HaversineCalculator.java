package de.hybris.platform.warehousing.sourcing.context.util;

public class HaversineCalculator
{
    private static final double RADIUS = 6372.8D;


    public static double calculate(double lat1, double lon1, double lat2, double lon2)
    {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double a = Math.sin(dLat / 2.0D) * Math.sin(dLat / 2.0D) + Math.sin(dLon / 2.0D) * Math.sin(dLon / 2.0D) * Math.cos(latitude1) * Math.cos(latitude2);
        double c = 2.0D * Math.asin(Math.sqrt(a));
        return 6372.8D * c;
    }
}
