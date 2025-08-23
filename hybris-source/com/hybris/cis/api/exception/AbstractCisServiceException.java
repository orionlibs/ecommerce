package com.hybris.cis.api.exception;

import com.hybris.cis.api.exception.codes.ServiceExceptionDetail;
import com.hybris.cis.api.service.CisService;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCisServiceException extends RuntimeException
{
    public static final long serialVersionUID = 783332842364593436L;
    private String vendorId;
    private String serviceId;
    private final transient List<ServiceExceptionDetail> errorCodes;


    protected AbstractCisServiceException(ServiceExceptionDetail errorCode)
    {
        this(Collections.singletonList(errorCode));
    }


    protected AbstractCisServiceException(ServiceExceptionDetail errorCode, Throwable cause)
    {
        this(Collections.singletonList(errorCode), cause);
    }


    protected AbstractCisServiceException(List<ServiceExceptionDetail> errorCodes)
    {
        super(formatErrors(errorCodes));
        this.errorCodes = errorCodes;
    }


    protected AbstractCisServiceException(List<ServiceExceptionDetail> errorCodes, Throwable cause)
    {
        super(formatErrors(errorCodes), cause);
        this.errorCodes = errorCodes;
    }


    private static String formatErrors(List<ServiceExceptionDetail> errorCodes)
    {
        StringBuilder message = new StringBuilder();
        for(ServiceExceptionDetail error : errorCodes)
        {
            message.append(error.getMessage()).append("\n");
        }
        return message.substring(0, message.length() - 1);
    }


    public void setService(CisService service)
    {
        this.serviceId = service.getType().toString();
        this.vendorId = service.getId();
    }


    public String getVendorId()
    {
        return this.vendorId;
    }


    public String getServiceId()
    {
        return this.serviceId;
    }


    public List<ServiceExceptionDetail> getErrorCodes()
    {
        return this.errorCodes;
    }
}
