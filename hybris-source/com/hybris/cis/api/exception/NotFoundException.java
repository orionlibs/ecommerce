package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCode;
import com.hybris.cis.api.exception.codes.StandardServiceExceptionCodes;
import java.util.List;

public class NotFoundException extends AbstractCisClientException
{
    private static final long serialVersionUID = 687700316762411995L;


    public NotFoundException(List<ServiceExceptionDetail> serviceExceptionDetails)
    {
        super(serviceExceptionDetails);
    }


    public NotFoundException(String resourceId)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.NOT_FOUND, resourceId));
    }


    public NotFoundException(String resourceId, Throwable cause)
    {
        super(new ServiceExceptionDetail((StandardServiceExceptionCode)StandardServiceExceptionCodes.NOT_FOUND, resourceId), cause);
    }


    public NotFoundException(String shortDescription, String reasonCode)
    {
        this(shortDescription + " (reasonCode=" + shortDescription + ")");
    }


    public NotFoundException(String id, String sourceSimpleClassName, String requestId, String reasonCode)
    {
        this(id + " (request type=" + id + ", requestId=" + sourceSimpleClassName + ", profileId=" + requestId + ", reasonCode=" + id + ")");
    }
}
