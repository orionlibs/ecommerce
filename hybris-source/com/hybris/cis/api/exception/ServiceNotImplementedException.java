package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class ServiceNotImplementedException extends AbstractCisClientException
{
    private static final long serialVersionUID = -8031191096903241146L;


    public ServiceNotImplementedException(List<ServiceExceptionDetail> serviceExceptionDetails)
    {
        super(serviceExceptionDetails);
    }


    public ServiceNotImplementedException(String function)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.NOT_IMPLEMENTED, function));
    }
}
