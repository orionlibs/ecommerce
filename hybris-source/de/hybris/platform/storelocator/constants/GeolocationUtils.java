package de.hybris.platform.storelocator.constants;

import de.hybris.platform.storelocator.exception.GeoLocatorException;
import java.util.Objects;

public class GeolocationUtils
{
    private static final String IS_INVALID = " is invalid";


    public static boolean validateDMSLatitude(String latitude)
    {
        if(latitude == null)
        {
            throw new GeoLocatorException("Latitude is required");
        }
        if(!latitude.matches("^\\s*(0?\\d|0?[1-8]\\d|90)°(\\s*(0?\\d|[1-5]\\d)'(\\s*(0?\\d|[1-5]\\d)\")?)?\\s*[N|S]\\s*$"))
        {
            throw new GeoLocatorException("Latitude: " + latitude + " is invalid");
        }
        String trmdLatitude = latitude.trim();
        String latitudeDegrees = trmdLatitude.substring(0, trmdLatitude.indexOf("°")).trim();
        String theRest = trmdLatitude.substring(trmdLatitude.indexOf("°")).trim();
        if(Integer.parseInt(latitudeDegrees) == 90 && theRest.matches("(.*)[1-9](.*)"))
        {
            throw new GeoLocatorException("Latitude [dms] " + trmdLatitude + " is invalid");
        }
        return true;
    }


    public static boolean validateDMSLongitude(String longitude)
    {
        if(longitude == null)
        {
            throw new GeoLocatorException("Longitude is required");
        }
        if(!longitude.matches("^\\s*(0?0?\\d|0?[1-9]\\d|1[0-7]\\d|180)°(\\s*(0?\\d|[1-5]\\d)'(\\s*(0?\\d|[1-5]\\d)\")?)?\\s*[W|E]\\s*$"))
        {
            throw new GeoLocatorException("Longitude " + longitude + " is invalid");
        }
        String trmdLongitude = longitude.trim();
        String latitudeDegrees = trmdLongitude.substring(0, trmdLongitude.indexOf("°")).trim();
        String theRest = trmdLongitude.substring(trmdLongitude.indexOf("°")).trim();
        if(Integer.parseInt(latitudeDegrees) == 180 && theRest.matches("(.*)[1-9](.*)"))
        {
            throw new GeoLocatorException("Longitude [dms] " + trmdLongitude + " is invalid");
        }
        return true;
    }


    public static boolean validateLongitude(double longitude)
    {
        if(longitude < -180.0D || longitude > 180.0D)
        {
            throw new GeoLocatorException("Longitude out of range :" + longitude);
        }
        return true;
    }


    public static boolean validateLatitude(double latitude)
    {
        if(latitude < -90.0D || latitude > 90.0D)
        {
            throw new GeoLocatorException("Latitude out of range :" + latitude);
        }
        return true;
    }


    public static int[] separateDMS(String dms)
    {
        int sign = 1;
        int[] result = {0, 0, 0};
        String noSpacesDms = removeSpaces(dms);
        String degrees = getSubstringBetween(noSpacesDms, null, "°");
        if(degrees != null)
        {
            result[0] = Integer.parseInt(degrees);
        }
        String minutes = getSubstringBetween(noSpacesDms, "°", "'");
        if(minutes != null)
        {
            result[1] = Integer.parseInt(minutes);
        }
        String seconds = getSubstringBetween(noSpacesDms, "'", "\"");
        if(seconds != null)
        {
            result[2] = Integer.parseInt(seconds);
        }
        if(Objects.nonNull(noSpacesDms) && (noSpacesDms.contains("S") || dms.contains("W")))
        {
            sign *= -1;
        }
        result[0] = result[0] * sign;
        return result;
    }


    protected static String removeSpaces(String input)
    {
        return input.replaceAll("\\s", "");
    }


    protected static String getSubstringBetween(String input, String subFrom, String subTo)
    {
        String substring = input;
        if(subFrom != null)
        {
            if(!input.contains(subFrom))
            {
                return null;
            }
            substring = input.substring(input.indexOf(subFrom) + 1);
        }
        if(subTo != null)
        {
            if(!substring.contains(subTo))
            {
                return null;
            }
            substring = substring.substring(0, substring.indexOf(subTo));
        }
        return substring;
    }
}
