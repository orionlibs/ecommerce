package com.hybris.cis.api.exception.codes;

public enum WebServiceExceptionCodes implements StandardServiceExceptionCode
{
    UNMARSHALL_EXCEPTION(9000, "Exception during unmarshalling"),
    MISSING_CLIENT_REF_ID(9001, "Client reference ID not found");
    private final int code;
    private final String message;


    WebServiceExceptionCodes(int code, String message)
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
}
