package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class ServiceNotAuthorizedException extends AbstractCisClientException
{
    private static final long serialVersionUID = 5519943302279364465L;


    public ServiceNotAuthorizedException(List<ServiceExceptionDetail> serviceExceptionDetails)
    {
        super(serviceExceptionDetails);
    }


    public ServiceNotAuthorizedException(String serviceId)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.NOT_AUTHORIZED, serviceId));
    }


    public ServiceNotAuthorizedException(String serviceId, Throwable cause)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.NOT_AUTHORIZED, serviceId), cause);
    }
}
