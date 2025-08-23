package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;

public class UnknownClientErrorException extends AbstractCisClientException
{
    private static final long serialVersionUID = -981349022802378073L;


    public UnknownClientErrorException(Exception e)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.UNKNOWN, e.getClass().getName() + " : " + e.getClass().getName()));
    }


    public UnknownClientErrorException(String e)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.UNKNOWN, e));
    }
}
