package com.hybris.datahub.validation;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ValidationFailure")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidationFailure implements Serializable
{
    private String message;
    private String propertyName;
    private ValidationFailureType failureType;


    public ValidationFailure()
    {
        this(null, null, null);
    }


    public ValidationFailure(String property, String message)
    {
        this(property, message, ValidationFailureType.EXCEPTION);
    }


    public ValidationFailure(String msg, ValidationFailureType severity)
    {
        this(null, msg, severity);
    }


    public ValidationFailure(String property, String msg, ValidationFailureType type)
    {
        this.message = msg;
        this.propertyName = property;
        this.failureType = type;
    }


    public String getMessage()
    {
        return this.message;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }


    public String getPropertyName()
    {
        return this.propertyName;
    }


    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }


    public ValidationFailureType getFailureType()
    {
        return this.failureType;
    }


    public void setFailureType(ValidationFailureType failureType)
    {
        this.failureType = failureType;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o instanceof ValidationFailure)
        {
            ValidationFailure that = (ValidationFailure)o;
            return (Objects.equals(this.propertyName, that.propertyName) && Objects.equals(this.message, that.message) && Objects.equals(this.failureType, that.failureType));
        }
        return false;
    }


    public int hashCode()
    {
        int result = (this.message != null) ? this.message.hashCode() : 0;
        result = 31 * result + ((this.propertyName != null) ? this.propertyName.hashCode() : 0);
        return result;
    }


    public String toString()
    {
        String property = (this.propertyName != null) ? (this.propertyName + ": ") : "";
        String msg = (this.message != null) ? ("\"" + this.message + "\"") : "";
        String type = (this.failureType != null) ? (this.failureType.toString() + ", ") : "";
        return "ValidationFailure{" + type + property + msg + "}";
    }
}
