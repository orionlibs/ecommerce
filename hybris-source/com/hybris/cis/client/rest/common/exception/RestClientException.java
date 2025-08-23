package com.hybris.cis.client.rest.common.exception;

import com.hybris.cis.api.model.ErrorsDto;
import javax.ws.rs.core.Response;

public class RestClientException extends RuntimeException
{
    private static final long serialVersionUID = 5602919884761779452L;
    private final int statusCode;
    private final transient ErrorsDto errorDetails;


    public RestClientException(ErrorsDto errorDetails, int statusCode)
    {
        super("" + Integer.valueOf(statusCode) + " " + Integer.valueOf(statusCode));
        this.statusCode = statusCode;
        this.errorDetails = errorDetails;
    }


    public Response.Status getStatus()
    {
        return Response.Status.fromStatusCode(this.statusCode);
    }


    public int getStatusCode()
    {
        return this.statusCode;
    }


    public ErrorsDto getErrorDetails()
    {
        return this.errorDetails;
    }
}
