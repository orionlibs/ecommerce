/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation;

import com.hybris.cockpitng.validation.model.ValidationGroup;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import java.util.List;

/**
 * Contains initial information about validation groups and confirmed violations.
 */
public interface ValidationContext
{
    List<ValidationGroup> getConstraintGroups();


    List<ValidationInfo> getConfirmed(ValidationSeverity severity);
}
