/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.labels.HyperlinkFallbackLabelProvider;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;

/**
 * Default implementation of {@link HyperlinkFallbackLabelProvider}
 */
public class DefaultHyperlinkFallbackLabelProvider implements HyperlinkFallbackLabelProvider
{
    private static final String HYPERLINK_FALLBACK_KEY = "hyperlink.fallback";
    private static final String HYPERLINK_LABEL_DEFAULT_VALUE = "Link";


    @Override
    public String getFallback(final String label)
    {
        return StringUtils.isBlank(label) ? getFallbackLabel() : label;
    }


    protected String getFallbackLabel()
    {
        return "[" + Labels.getLabel(HYPERLINK_FALLBACK_KEY, HYPERLINK_LABEL_DEFAULT_VALUE) + "]";
    }
}
