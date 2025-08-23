package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import java.util.List;

public class ServicePreconditionFailedException extends AbstractCisClientException
{
    private static final long serialVersionUID = 8971841288042730453L;


    public ServicePreconditionFailedException(ServiceExceptionDetail errorCode)
    {
        super(errorCode);
    }


    public ServicePreconditionFailedException(ServiceExceptionDetail errorCode, Throwable cause)
    {
        super(errorCode, cause);
    }


    public ServicePreconditionFailedException(List<ServiceExceptionDetail> errorCodes)
    {
        super(errorCodes);
    }


    public ServicePreconditionFailedException(List<ServiceExceptionDetail> errorCodes, Throwable cause)
    {
        super(errorCodes, cause);
    }
}
