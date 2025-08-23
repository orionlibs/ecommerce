/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.classification.labels.impl;

import com.hybris.cockpitng.labels.LabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.Range;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

/**
 * Label Provider for Ranges. It delegates label resolution to * {@link com.hybris.cockpitng.labels.LabelService}.
 */
public class RangeLabelProvider implements LabelProvider<Range>
{
    /**
     * Key of a label to be displayed when there is no value for given attribute
     */
    private static final String LABEL_KEY_NO_VALUE = "data.no.value";
    private static final String LABEL_KEY_RANGE_FROM = "range.from";
    private static final String DEFAULT_VALUE_RANGE_FROM = "from";
    private static final String LABEL_KEY_RANGE_TO = "range.to";
    private static final String DEFAULT_VALUE_RANGE_TO = "to";
    private static final String RANGE_LABEL_PATTERN = "%s %s %s %s";
    private LabelService labelService;


    @Override
    public String getLabel(final Range range)
    {
        if(range != null)
        {
            final String labelFrom = getLabelFrom();
            final String labelTo = getLabelTo();
            final Object start = range.getStart();
            final String fromValue = start == null ? getDefaultNoValue() : getStringValue(start);
            final Object end = range.getEnd();
            final String toValue = end == null ? getDefaultNoValue() : getStringValue(end);
            return String.format(RANGE_LABEL_PATTERN, labelFrom, fromValue, labelTo, toValue);
        }
        return StringUtils.EMPTY;
    }


    protected String getDefaultNoValue()
    {
        return Labels.getLabel(LABEL_KEY_NO_VALUE);
    }


    protected String getLabelFrom()
    {
        return Labels.getLabel(LABEL_KEY_RANGE_FROM, DEFAULT_VALUE_RANGE_FROM);
    }


    protected String getLabelTo()
    {
        return Labels.getLabel(LABEL_KEY_RANGE_TO, DEFAULT_VALUE_RANGE_TO);
    }


    protected String getStringValue(final Object object)
    {
        final String resultLabel = (object instanceof String) ? object.toString() : getLabelService().getObjectLabel(object);
        return resultLabel != null ? resultLabel : StringUtils.EMPTY;
    }


    @Override
    public String getDescription(final Range range)
    {
        return null;
    }


    @Override
    public String getIconPath(final Range range)
    {
        return null;
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}
