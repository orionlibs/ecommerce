package de.hybris.platform.webservicescommons.validators;

import org.springframework.validation.Validator;

public abstract class AbstractFieldValidator implements Validator
{
    private String fieldPath;


    public String getFieldPath()
    {
        return this.fieldPath;
    }


    public void setFieldPath(String fieldPath)
    {
        this.fieldPath = fieldPath;
    }
}
