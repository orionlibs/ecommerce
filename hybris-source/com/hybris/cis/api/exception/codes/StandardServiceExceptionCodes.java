package com.hybris.cis.api.exception.codes;

public enum StandardServiceExceptionCodes implements StandardServiceExceptionCode
{
    UNKNOWN(0, "An unknown error occured"),
    NOT_FOUND(404, "Resource not found"),
    NOT_AUTHORIZED(403, "The user is not authorized to use the service or resource"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    NO_SUCH_SERVICE(500, "No such service"),
    NO_CONFIGURATION_FOUND(500, "No cis configuration found"),
    NOT_IMPLEMENTED(501, "The service or function is not implemented"),
    NOT_AVAILABLE(503, "The service is currently not available"),
    TIMEOUT(504, "A service timeout occured"),
    INVALID_OR_MISSING_FIELD(1000, "Invalid or missing field"),
    NOT_VALID(1001, "The service id is invalid for the type of service requested"),
    INCOMPLETE_SERVICE_CONFIGURATION(1002, "The service configuration is missing a required value"),
    ERROR_RESPONSE_FROM_SERVICE(1003, "The 3rd party service returned an error"),
    UNREADABLE_RESPONSE(1004, "Unreadable response came from the 3rd party"),
    UNREADABLE_REQUEST(1005, "Unreadable request was send to CIS");
    private final int code;
    private final String message;


    StandardServiceExceptionCodes(int code, String message)
    {
        this.code = code;
        this.message = message;
    }


    public int getCode()
    {
        return this.code;
    }


    public String getMessage()
    {
        return this.message;
    }


    public String toString()
    {
        return "" + this.code + " - " + this.code;
    }


    public static StandardServiceExceptionCode fromCode(String c)
    {
        for(StandardServiceExceptionCode standardServiceExceptionCode : values())
        {
            if(String.valueOf(standardServiceExceptionCode.getCode()).equalsIgnoreCase(c))
            {
                return standardServiceExceptionCode;
            }
        }
        throw new IllegalArgumentException("No enum value found for: " + c);
    }
}
