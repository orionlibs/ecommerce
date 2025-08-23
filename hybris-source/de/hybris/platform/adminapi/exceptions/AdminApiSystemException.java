package de.hybris.platform.adminapi.exceptions;

import de.hybris.platform.servicelayer.exceptions.SystemException;
import org.springframework.http.HttpStatus;

public class AdminApiSystemException extends SystemException
{
    private final HttpStatus httpStatus;


    public AdminApiSystemException(String message, HttpStatus httpStatus)
    {
        super(message);
        this.httpStatus = httpStatus;
    }


    public AdminApiSystemException(Throwable cause, HttpStatus httpStatus)
    {
        super(cause);
        this.httpStatus = httpStatus;
    }


    public AdminApiSystemException(String message, Throwable cause, HttpStatus httpStatus)
    {
        super(message, cause);
        this.httpStatus = httpStatus;
    }


    public HttpStatus getHttpStatus()
    {
        return this.httpStatus;
    }
}
