package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import java.util.List;

public abstract class AbstractCisClientException extends AbstractCisServiceException
{
    private static final long serialVersionUID = -3321473190997866558L;


    protected AbstractCisClientException(ServiceExceptionDetail errorCode)
    {
        super(errorCode);
    }


    protected AbstractCisClientException(ServiceExceptionDetail errorCode, Throwable cause)
    {
        super(errorCode, cause);
    }


    protected AbstractCisClientException(List<ServiceExceptionDetail> errorCodes)
    {
        super(errorCodes);
    }


    protected AbstractCisClientException(List<ServiceExceptionDetail> errorCodes, Throwable cause)
    {
        super(errorCodes, cause);
    }
}
