package de.hybris.platform.cmsfacades.validator.data;

import java.io.Serializable;

public class ValidationError implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String field;
    private Object rejectedValue;
    private String language;
    private String errorCode;
    private Object[] errorArgs;
    private String exceptionMessage;
    private String defaultMessage;
    private Integer position;


    public void setField(String field)
    {
        this.field = field;
    }


    public String getField()
    {
        return this.field;
    }


    public void setRejectedValue(Object rejectedValue)
    {
        this.rejectedValue = rejectedValue;
    }


    public Object getRejectedValue()
    {
        return this.rejectedValue;
    }


    public void setLanguage(String language)
    {
        this.language = language;
    }


    public String getLanguage()
    {
        return this.language;
    }


    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }


    public String getErrorCode()
    {
        return this.errorCode;
    }


    public void setErrorArgs(Object[] errorArgs)
    {
        this.errorArgs = errorArgs;
    }


    public Object[] getErrorArgs()
    {
        return this.errorArgs;
    }


    public void setExceptionMessage(String exceptionMessage)
    {
        this.exceptionMessage = exceptionMessage;
    }


    public String getExceptionMessage()
    {
        return this.exceptionMessage;
    }


    public void setDefaultMessage(String defaultMessage)
    {
        this.defaultMessage = defaultMessage;
    }


    public String getDefaultMessage()
    {
        return this.defaultMessage;
    }


    public void setPosition(Integer position)
    {
        this.position = position;
    }


    public Integer getPosition()
    {
        return this.position;
    }
}
