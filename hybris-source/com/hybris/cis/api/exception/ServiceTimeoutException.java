package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class ServiceTimeoutException extends AbstractServiceErrorException
{
    private static final long serialVersionUID = -620153758083015833L;


    public ServiceTimeoutException(String message)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.TIMEOUT, message));
    }


    public ServiceTimeoutException(Throwable cause)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.TIMEOUT, cause.getMessage()), cause);
    }


    public ServiceTimeoutException(List<ServiceExceptionDetail> serviceExceptionDetail)
    {
        super(serviceExceptionDetail);
    }
}
