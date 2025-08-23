package de.hybris.platform.storelocator.constants;

import de.hybris.platform.storelocator.exception.GeoLocatorException;

public class GeolocationMaths
{
    public static final int SIGNIFICANT_DIGITS = 6;
    public static final int SCALE_DIGITS = 60;


    public static int[] decimal2DMS(double decimal)
    {
        GeolocationUtils.validateLongitude(decimal);
        int[] dmc = {0, 0, 0};
        int sign = (decimal > 0.0D) ? 1 : -1;
        decimal = Math.abs(decimal);
        dmc[0] = (int)Math.floor(decimal);
        decimal -= dmc[0];
        decimal *= 60.0D;
        dmc[1] = (int)Math.floor(Math.abs(decimal));
        decimal -= dmc[1];
        decimal *= 60.0D;
        dmc[2] = (int)Math.round(Math.abs(decimal));
        dmc[0] = dmc[0] * sign;
        return dmc;
    }


    public static double dms2Decimal(int[] dms)
    {
        if(dms == null || dms.length != 3)
        {
            throw new GeoLocatorException("Invalid Degree, Minutes, Seconds format");
        }
        int sign = (dms[0] > 0) ? 1 : -1;
        double decimal = 0.0D;
        decimal += Math.abs(dms[0]);
        double secondsTotal = dms[1] * 60.0D + dms[2];
        decimal += secondsTotal / 3600.0D;
        return truncateDecimal(decimal) * sign;
    }


    public static double truncateDecimal(double decimal)
    {
        double factor = Math.pow(10.0D, 6.0D);
        return Math.rint(decimal * factor) / factor;
    }
}
