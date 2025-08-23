package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.UnknownServiceExceptionDetail;
import java.util.List;

public abstract class AbstractServiceErrorException extends AbstractCisServiceException
{
    private static final long serialVersionUID = -30883185866709408L;


    protected AbstractServiceErrorException(String errorMsg)
    {
        super((ServiceExceptionDetail)new UnknownServiceExceptionDetail(errorMsg));
    }


    protected AbstractServiceErrorException(ServiceExceptionDetail errorCode)
    {
        super(errorCode);
    }


    protected AbstractServiceErrorException(ServiceExceptionDetail errorCode, Throwable cause)
    {
        super(errorCode, cause);
    }


    protected AbstractServiceErrorException(List<ServiceExceptionDetail> errorCodes)
    {
        super(errorCodes);
    }


    protected AbstractServiceErrorException(List<ServiceExceptionDetail> errorCodes, Throwable cause)
    {
        super(errorCodes, cause);
    }
}
