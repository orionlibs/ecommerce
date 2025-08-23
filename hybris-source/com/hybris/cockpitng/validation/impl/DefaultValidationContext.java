/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.impl;

import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.model.ValidationGroup;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import java.util.Collections;
import java.util.List;

public class DefaultValidationContext implements ValidationContext
{
    private ValidationResult confirmed;
    private List<ValidationGroup> constraintGroups;


    public DefaultValidationContext()
    {
        this(null);
    }


    public DefaultValidationContext(final ValidationResult confirmed)
    {
        this.confirmed = confirmed;
    }


    @Override
    public List<ValidationGroup> getConstraintGroups()
    {
        return constraintGroups;
    }


    public List<ValidationInfo> getConfirmed(final ValidationSeverity severity)
    {
        return confirmed != null ? confirmed.get(severity).collect() : Collections.emptyList();
    }


    public void setConstraintGroups(final List<ValidationGroup> constraintGroups)
    {
        this.constraintGroups = Collections.unmodifiableList(constraintGroups);
    }


    public void setConfirmed(final ValidationResult confirmed)
    {
        this.confirmed = confirmed;
    }
}
