/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.model;

public enum ValidationSeverity
{
    ERROR("ERROR"), WARN("WARN"), INFO("INFO"), NONE("NONE");
    private final String code;


    ValidationSeverity(final String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public boolean isLowerThan(final ValidationSeverity severity)
    {
        return ordinal() > severity.ordinal();
    }


    public boolean isHigherThan(final ValidationSeverity severity)
    {
        return severity.ordinal() > ordinal();
    }
}