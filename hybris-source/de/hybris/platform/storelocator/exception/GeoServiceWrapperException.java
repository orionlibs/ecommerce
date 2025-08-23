package de.hybris.platform.storelocator.exception;

import java.util.HashMap;
import java.util.Map;

public class GeoServiceWrapperException extends RuntimeException
{
    private final String googleResponseCode;
    public static final String G_GEO_BAD_REQUEST = "400";
    public static final String G_GEO_SERVER_ERROR = "500";
    public static final String G_GEO_MISSING_QUERY = "601";
    public static final String G_GEO_MISSING_ADDRESS = "601";
    public static final String G_GEO_UNKNOWN_ADDRESS = "602";
    public static final String G_GEO_UNAVAILABLE_ADDRESS = "603";
    public static final String G_GEO_UNKNOWN_DIRECTIONS = "604";
    public static final String G_GEO_REQUST_DENIED = "REQUEST_DENIED";
    public static final String G_GEO_BAD_KEY = "610";
    public static final String G_GEO_TOO_MANY_QUERIES = "620";
    private static final Map<String, String> errorMessages = new HashMap<>();

    static
    {
        errorMessages.put("REQUEST_DENIED", "A directions request denied");
        errorMessages.put("400", "A directions request could not be successfully parsed");
        errorMessages.put("500", "Google service unreachable");
        errorMessages.put("601", "The HTTP q parameter was either missing or had no value");
        errorMessages.put("601", "The address in query was either missing or had no value");
        errorMessages.put("602", "No corresponding geographic location could be found");
        errorMessages.put("603", "The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons");
        errorMessages.put("604", "Could not compute directions between the points mentioned");
        errorMessages.put("610", "The given key is either invalid or does not match the domain for which it was given");
        errorMessages.put("620", "The given key has gone over the requests limit");
    }

    public GeoServiceWrapperException()
    {
        this.googleResponseCode = null;
    }


    public GeoServiceWrapperException(String message, Throwable nested)
    {
        super(message, nested);
        this.googleResponseCode = null;
    }


    public GeoServiceWrapperException(String message)
    {
        super(message);
        this.googleResponseCode = null;
    }


    public GeoServiceWrapperException(Throwable nested)
    {
        super(nested);
        this.googleResponseCode = null;
    }


    public GeoServiceWrapperException(String message, String googleResponseCode)
    {
        super(message + message);
        this.googleResponseCode = googleResponseCode;
    }


    public String getGoogleResponseCode()
    {
        return this.googleResponseCode;
    }


    public static String getErrorMessagesDesc(String name)
    {
        return errorMessages.get(name);
    }
}
