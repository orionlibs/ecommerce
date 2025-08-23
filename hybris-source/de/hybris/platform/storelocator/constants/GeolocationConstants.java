package de.hybris.platform.storelocator.constants;

public final class GeolocationConstants
{
    public static final int TEXTUAL_ADDRESS_MAX_LINELENGHT = 50;
    public static final int TEXTUAL_ADDRESS_MAX_LINES = 5;
    public static final String TEXTUAL_ADDRESS_LINE_DELIMITER = "|";
    public static final String TEXTUAL_ADDRESS_ALLOWED_CHARS_COMMON = "0-9a-zA-Z.-:/\\s\\t\\n";
    public static final String DMS_DEGREE = "°";
    public static final String DMS_MINUTES = "'";
    public static final String DMS_SECONDS = "\"";
    private static final String DMS_0_180 = "(0?0?\\d|0?[1-9]\\d|1[0-7]\\d|180)";
    private static final String DMS_0_90 = "(0?\\d|0?[1-8]\\d|90)";
    private static final String DMS_0_59 = "(0?\\d|[1-5]\\d)";
    private static final String DMS_SECONDS_PART = "(\\s*(0?\\d|[1-5]\\d)\")?";
    private static final String DMS_MINUTES_SECONDS_PART = "(\\s*(0?\\d|[1-5]\\d)'(\\s*(0?\\d|[1-5]\\d)\")?)?";
    public static final String ERROR_MSG_PREFIX = "geolocation.error.";
    public static final String DECIMAL_COORDINATES_FORMAT = ".######";
    public static final String DMS_LONGITUDE_PATTERN = "^\\s*(0?0?\\d|0?[1-9]\\d|1[0-7]\\d|180)°(\\s*(0?\\d|[1-5]\\d)'(\\s*(0?\\d|[1-5]\\d)\")?)?\\s*[W|E]\\s*$";
    public static final String DMS_LATITUDE_PATTERN = "^\\s*(0?\\d|0?[1-8]\\d|90)°(\\s*(0?\\d|[1-5]\\d)'(\\s*(0?\\d|[1-5]\\d)\")?)?\\s*[N|S]\\s*$";
    public static final String GEOCODEJOB_BEAN_ID = "geocodeAddressesJob";
    public static final String GEOCODECRONJOB_CODE = "Geocode POS Addresses";
}
