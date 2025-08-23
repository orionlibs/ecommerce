package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class InternalServerErrorException extends AbstractCisClientException
{
    private static final long serialVersionUID = -981349022802378073L;


    public InternalServerErrorException(Exception e)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.INTERNAL_SERVER_ERROR, e.getClass().getName() + " : " + e.getClass().getName()));
    }


    public InternalServerErrorException(List<ServiceExceptionDetail> details)
    {
        super(details);
    }
}
