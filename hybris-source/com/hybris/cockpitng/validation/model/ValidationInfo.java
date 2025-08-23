/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.model;

import java.util.Comparator;

/**
 * Represents single validation violation
 */
public interface ValidationInfo
{
    /**
     * Checks whether validation violation is already confirmed by user
     *
     * @return <code>true</code> if violation has been confirmed
     */
    boolean isConfirmed();


    /**
     * Sets confirmation status of validation violation.
     *
     * @param confirmed <code>true</code> if violation should be treated as confirmed
     */
    void setConfirmed(boolean confirmed);


    /**
     *
     * @return severity of validation violation
     */
    ValidationSeverity getValidationSeverity();


    /**
     * Get user-friendly description of validation violation
     *
     * @return description of violation
     */
    String getValidationMessage();


    /**
     *
     * @return value that violates validation
     */
    Object getInvalidValue();


    /**
     *
     * @return path to attribute that violates validation
     */
    String getInvalidPropertyPath();


    Comparator<ValidationInfo> COMPARATOR_SEVERITY_DESC = (o1, o2) -> {
        if(o1.getValidationSeverity().equals(o2.getValidationSeverity()))
        {
            return Boolean.valueOf(o1.isConfirmed()).compareTo(o2.isConfirmed());
        }
        else
        {
            return o1.getValidationSeverity().compareTo(o2.getValidationSeverity());
        }
    };
}
