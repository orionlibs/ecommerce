package com.hybris.backoffice.excel.data;

import java.io.Serializable;

public class ImpexValue implements Serializable
{
    private final transient Object value;
    private final ImpexHeaderValue headerValue;


    public ImpexValue(Object value, ImpexHeaderValue headerValue)
    {
        this.value = value;
        this.headerValue = headerValue;
    }


    public Object getValue()
    {
        return this.value;
    }


    public ImpexHeaderValue getHeaderValue()
    {
        return this.headerValue;
    }
}
