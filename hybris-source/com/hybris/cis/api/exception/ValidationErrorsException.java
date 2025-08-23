package com.hybris.cis.api.exception;

import org.springframework.validation.Errors;

public class ValidationErrorsException extends RuntimeException
{
    private final transient Errors errors;


    public ValidationErrorsException(Errors errors)
    {
        this.errors = errors;
    }


    public Errors getErrors()
    {
        return this.errors;
    }
}
