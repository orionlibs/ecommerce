/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.services.impl;

import com.hybris.backoffice.services.ClassificationLabelService;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

/**
 * Default implementation of {@link ClassificationLabelService}, where {@link ClassificationLabelService#getClassificationLabel(String, Locale)} always returns empty string.
 */
public class DefaultClassificationLabelService implements ClassificationLabelService
{
    @Override
    public String getClassificationLabel(final String attributeQualifier, final Locale locale)
    {
        return StringUtils.EMPTY;
    }
}
