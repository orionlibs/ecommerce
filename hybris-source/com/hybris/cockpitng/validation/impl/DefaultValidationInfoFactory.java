/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.impl;

import com.hybris.cockpitng.validation.ValidationInfoFactory;
import com.hybris.cockpitng.validation.model.ValidationInfo;

/**
 * Factory with no wrapping functionality.
 *
 */
public class DefaultValidationInfoFactory implements ValidationInfoFactory
{
    @Override
    public ValidationInfo createValidationInfo(final ValidationInfo validationInfo)
    {
        return validationInfo;
    }
}
