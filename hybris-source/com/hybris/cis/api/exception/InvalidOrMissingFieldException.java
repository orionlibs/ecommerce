package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class InvalidOrMissingFieldException extends ServicePreconditionFailedException
{
    private static final long serialVersionUID = -8031191096903241146L;


    public InvalidOrMissingFieldException(String field)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.INVALID_OR_MISSING_FIELD, field));
    }


    public InvalidOrMissingFieldException(List<ServiceExceptionDetail> details)
    {
        super(details);
    }
}
