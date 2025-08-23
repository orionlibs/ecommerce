/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels;

/**
 * Hyperlinks are bound with labels, but there are cases where label with hyperlink are empty and there is no
 * possibility to click on it. In such cases a fallback label should be used.
 */
public interface HyperlinkFallbackLabelProvider
{
    /**
     * Returns fallback label for hyperlink
     *
     * @param label
     *           which should be bound with hyperlink
     * @return input if label is not blank or fallback value when input is blank
     */
    String getFallback(final String label);
}
