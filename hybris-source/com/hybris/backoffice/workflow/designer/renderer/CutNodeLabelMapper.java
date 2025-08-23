/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

/**
 * Implementation of {@link NodeLabelMapper} which maps a node's label. The label is cut if its length exceeds value
 * defined by {@link #maxLength} field
 */
class CutNodeLabelMapper implements NodeLabelMapper
{
    private static final int DEFAULT_MAX_LENGTH = 10;
    private int maxLength = DEFAULT_MAX_LENGTH;


    @Override
    public String apply(final String label)
    {
        final String suffix = "...";
        return label.length() > maxLength ? label.substring(0, maxLength).concat(suffix) : label;
    }


    // optional
    public void setMaxLength(final int maxLength)
    {
        this.maxLength = maxLength;
    }
}
