/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.impl;

import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;

public class DefaultValidationInfo implements ValidationInfo
{
    private ValidationSeverity validationSeverity;
    private String validationMessage;
    private Object invalidValue;
    private String invalidPropertyPath;
    private boolean confirmed;


    @Override
    public boolean isConfirmed()
    {
        return confirmed;
    }


    @Override
    public void setConfirmed(final boolean confirmed)
    {
        this.confirmed = confirmed;
    }


    @Override
    public ValidationSeverity getValidationSeverity()
    {
        return validationSeverity;
    }


    public void setValidationSeverity(final ValidationSeverity validationSeverity)
    {
        this.validationSeverity = validationSeverity;
    }


    @Override
    public String getValidationMessage()
    {
        return validationMessage;
    }


    public void setValidationMessage(final String validationMessage)
    {
        this.validationMessage = validationMessage;
    }


    @Override
    public Object getInvalidValue()
    {
        return invalidValue;
    }


    public void setInvalidValue(final Object invalidValue)
    {
        this.invalidValue = invalidValue;
    }


    @Override
    public String getInvalidPropertyPath()
    {
        return invalidPropertyPath;
    }


    public void setInvalidPropertyPath(final String invalidPropertyPath)
    {
        this.invalidPropertyPath = invalidPropertyPath;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o instanceof DefaultValidationInfo)
        {
            final DefaultValidationInfo that = (DefaultValidationInfo)o;
            if(getValidationSeverity() != that.getValidationSeverity())
            {
                return false;
            }
            if(getValidationMessage() != null ? !getValidationMessage().equals(that.getValidationMessage())
                            : that.getValidationMessage() != null)
            {
                return false;
            }
            return !(getInvalidPropertyPath() != null ? !getInvalidPropertyPath().equals(that.getInvalidPropertyPath())
                            : that.getInvalidPropertyPath() != null);
        }
        else
        {
            return false;
        }
    }


    @Override
    public int hashCode()
    {
        int result = getValidationSeverity() != null ? getValidationSeverity().hashCode() : 0;
        result = 31 * result + (getValidationMessage() != null ? getValidationMessage().hashCode() : 0);
        result = 31 * result + (getInvalidPropertyPath() != null ? getInvalidPropertyPath().hashCode() : 0);
        return result;
    }
}
