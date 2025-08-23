/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.services;

import java.util.Locale;

/**
 * Label service responsible for providing localized values for classification's attributes.
 */
public interface ClassificationLabelService
{
    /**
     * Finds localized label for given classification qualifier
     *
     * @param attributeQualifier qualifier of classification
     * @param locale an object which represents a specific geographical, political,
     * or cultural region
     * @return localized value for given qualifier
     */
    String getClassificationLabel(final String attributeQualifier, final Locale locale);
}
