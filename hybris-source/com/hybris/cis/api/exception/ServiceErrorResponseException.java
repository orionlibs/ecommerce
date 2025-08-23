package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.UnknownServiceExceptionDetail;
import java.util.List;

public class ServiceErrorResponseException extends AbstractServiceErrorException
{
    private static final long serialVersionUID = -6980142120552997382L;


    public ServiceErrorResponseException(String errorMsg)
    {
        super((ServiceExceptionDetail)new UnknownServiceExceptionDetail(errorMsg));
    }


    public ServiceErrorResponseException(ServiceExceptionDetail errorCode)
    {
        super(errorCode);
    }


    public ServiceErrorResponseException(ServiceExceptionDetail errorCode, Throwable cause)
    {
        super(errorCode, cause);
    }


    public ServiceErrorResponseException(List<ServiceExceptionDetail> errorCodes)
    {
        super(errorCodes);
    }


    public ServiceErrorResponseException(List<ServiceExceptionDetail> errorCodes, Throwable cause)
    {
        super(errorCodes, cause);
    }
}
