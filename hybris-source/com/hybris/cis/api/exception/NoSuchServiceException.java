package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class NoSuchServiceException extends AbstractCisServiceException
{
    private static final long serialVersionUID = 8384998861225823347L;


    public NoSuchServiceException(ServiceExceptionDetail serviceExceptionDetail)
    {
        super(serviceExceptionDetail);
    }


    public NoSuchServiceException(List<ServiceExceptionDetail> serviceExceptionDetails)
    {
        super(serviceExceptionDetails);
    }


    public NoSuchServiceException(Throwable cause)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.NO_SUCH_SERVICE, cause.getMessage()), cause);
    }
}
