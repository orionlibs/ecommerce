/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.jaxb;

/**
 * Represents validation status of schema based validation for a given xml content.
 */
public final class SchemaValidationStatus
{
    private final SchemaValidationCode code;
    private final String message;


    private SchemaValidationStatus(final SchemaValidationCode code)
    {
        this(code, null);
    }


    private SchemaValidationStatus(final SchemaValidationCode code, final String message)
    {
        this.code = code;
        this.message = message;
    }


    public static SchemaValidationStatus error()
    {
        return new SchemaValidationStatus(SchemaValidationCode.ERROR);
    }


    public static SchemaValidationStatus error(final String message)
    {
        return new SchemaValidationStatus(SchemaValidationCode.ERROR, message);
    }


    public static SchemaValidationStatus warning()
    {
        return new SchemaValidationStatus(SchemaValidationCode.WARNING);
    }


    public static SchemaValidationStatus warning(final String message)
    {
        return new SchemaValidationStatus(SchemaValidationCode.WARNING, message);
    }


    public static SchemaValidationStatus success()
    {
        return new SchemaValidationStatus(SchemaValidationCode.SUCCESS);
    }


    public static SchemaValidationStatus success(final String message)
    {
        return new SchemaValidationStatus(SchemaValidationCode.SUCCESS, message);
    }


    public SchemaValidationCode getCode()
    {
        return code;
    }


    public String getMessage()
    {
        return message;
    }


    public boolean isError()
    {
        return code == SchemaValidationCode.ERROR;
    }


    public boolean isWarning()
    {
        return code == SchemaValidationCode.WARNING;
    }


    public boolean isSuccess()
    {
        return code == SchemaValidationCode.SUCCESS;
    }


    public enum SchemaValidationCode
    {
        ERROR, WARNING, SUCCESS;


        @Override
        public String toString()
        {
            return name().toLowerCase();
        }
    }
}
