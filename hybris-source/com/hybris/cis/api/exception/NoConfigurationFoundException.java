package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class NoConfigurationFoundException extends AbstractCisServiceException
{
    private static final long serialVersionUID = 8384998861225823347L;


    public NoConfigurationFoundException(ServiceExceptionDetail serviceExceptionDetail)
    {
        super(serviceExceptionDetail);
    }


    public NoConfigurationFoundException(List<ServiceExceptionDetail> serviceExceptionDetails)
    {
        super(serviceExceptionDetails);
    }


    public NoConfigurationFoundException(Throwable cause)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.NO_CONFIGURATION_FOUND, cause.getMessage()), cause);
    }
}
