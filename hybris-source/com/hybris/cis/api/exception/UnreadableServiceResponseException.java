package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class UnreadableServiceResponseException extends AbstractServiceErrorException
{
    private static final long serialVersionUID = -6980142120552997382L;


    public UnreadableServiceResponseException(String errorMsg)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.UNREADABLE_RESPONSE, errorMsg));
    }


    public UnreadableServiceResponseException(Throwable throwable)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.UNREADABLE_RESPONSE), throwable);
    }


    public UnreadableServiceResponseException(ServiceExceptionDetail errorCode)
    {
        super(errorCode);
    }


    public UnreadableServiceResponseException(ServiceExceptionDetail errorCode, Exception e)
    {
        super(errorCode, e);
    }


    public UnreadableServiceResponseException(List<ServiceExceptionDetail> errorCodes)
    {
        super(errorCodes);
    }
}
