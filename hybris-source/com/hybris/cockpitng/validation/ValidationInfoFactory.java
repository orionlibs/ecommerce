/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation;

import com.hybris.cockpitng.validation.model.ValidationInfo;

/**
 * Factory able to create new validation info on basis of existing one
 */
public interface ValidationInfoFactory
{
    /**
     * Creates new validation info on basis of provided one
     *
     * @param validationInfo
     *           existing validation info
     * @return new validation info
     */
    ValidationInfo createValidationInfo(ValidationInfo validationInfo);
}
