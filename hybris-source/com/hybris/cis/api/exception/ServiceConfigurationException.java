package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class ServiceConfigurationException extends AbstractCisClientException
{
    private static final long serialVersionUID = -2303120384153187659L;


    public ServiceConfigurationException(List<ServiceExceptionDetail> serviceExceptionDetails)
    {
        super(serviceExceptionDetails);
    }


    public ServiceConfigurationException(String configurationValue)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.INCOMPLETE_SERVICE_CONFIGURATION, configurationValue));
    }
}
