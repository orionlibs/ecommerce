package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class ServiceNotAvailableException extends AbstractServiceErrorException
{
    private static final long serialVersionUID = 8384998861225823347L;


    public ServiceNotAvailableException(List<ServiceExceptionDetail> serviceExceptionDetails)
    {
        super(serviceExceptionDetails);
    }


    public ServiceNotAvailableException(Throwable cause)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.NOT_AVAILABLE, cause.getMessage()), cause);
    }
}
