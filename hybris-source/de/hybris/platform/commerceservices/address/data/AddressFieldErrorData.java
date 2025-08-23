package de.hybris.platform.commerceservices.address.data;

import java.io.Serializable;

public class AddressFieldErrorData<FIELD_TYPE, ERROR_CODE> implements Serializable
{
    private static final long serialVersionUID = 1L;
    private FIELD_TYPE fieldType;
    private ERROR_CODE errorCode;


    public void setFieldType(FIELD_TYPE fieldType)
    {
        this.fieldType = fieldType;
    }


    public FIELD_TYPE getFieldType()
    {
        return this.fieldType;
    }


    public void setErrorCode(ERROR_CODE errorCode)
    {
        this.errorCode = errorCode;
    }


    public ERROR_CODE getErrorCode()
    {
        return this.errorCode;
    }
}
