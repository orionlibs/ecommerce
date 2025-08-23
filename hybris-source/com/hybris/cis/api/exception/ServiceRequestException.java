package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import java.util.List;

public class ServiceRequestException extends AbstractCisClientException
{
    private static final long serialVersionUID = 5739425421118011132L;


    public ServiceRequestException(ServiceExceptionDetail errorCode)
    {
        super(errorCode);
    }


    public ServiceRequestException(ServiceExceptionDetail errorCode, Throwable cause)
    {
        super(errorCode, cause);
    }


    public ServiceRequestException(List<ServiceExceptionDetail> errorCodes)
    {
        super(errorCodes);
    }


    public ServiceRequestException(List<ServiceExceptionDetail> errorCodes, Throwable cause)
    {
        super(errorCodes, cause);
    }
}
