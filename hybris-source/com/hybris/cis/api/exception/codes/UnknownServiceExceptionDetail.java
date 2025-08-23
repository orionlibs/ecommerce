package com.hybris.cis.api.exception.codes;

public class UnknownServiceExceptionDetail extends ServiceExceptionDetail
{
    public UnknownServiceExceptionDetail(String message)
    {
        super((StandardServiceExceptionCode)StandardServiceExceptionCodes.UNKNOWN, message);
    }
}
