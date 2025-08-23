package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class UnreadableServiceRequestException extends AbstractServiceErrorException
{
    private static final long serialVersionUID = -6980142120552997382L;


    public UnreadableServiceRequestException(String errorMsg)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.UNREADABLE_RESPONSE, errorMsg));
    }


    public UnreadableServiceRequestException(Throwable throwable)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.UNREADABLE_RESPONSE), throwable);
    }


    public UnreadableServiceRequestException(ServiceExceptionDetail errorCode)
    {
        super(errorCode);
    }


    public UnreadableServiceRequestException(ServiceExceptionDetail errorCode, Exception e)
    {
        super(errorCode, e);
    }


    public UnreadableServiceRequestException(List<ServiceExceptionDetail> errorCodes)
    {
        super(errorCodes);
    }
}
