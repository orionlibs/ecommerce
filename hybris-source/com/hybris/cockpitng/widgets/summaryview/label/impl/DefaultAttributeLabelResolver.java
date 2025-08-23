/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview.label.impl;

import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.labels.LabelUtils;
import com.hybris.cockpitng.widgets.summaryview.AbstractAttributeLabelResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

public class DefaultAttributeLabelResolver extends AbstractAttributeLabelResolver
{
    private LabelService labelService;


    @Override
    public String getAttributeLabel(final Attribute attributeConfiguration, final DataAttribute attribute, final String typeCode)
    {
        final String labelKey = attributeConfiguration.getLabel();
        if(StringUtils.isNotBlank(labelKey))
        {
            return Labels.getLabel(labelKey, LabelUtils.getFallbackLabel(labelKey));
        }
        final String qualifier = attributeConfiguration.getQualifier();
        String label = getLabelService().getObjectLabel(resolveAttributePath(typeCode, qualifier));
        if(StringUtils.isBlank(label))
        {
            label = LabelUtils.getFallbackLabel(qualifier);
        }
        return label;
    }


    protected String resolveAttributePath(final String typeCode, final String attributeQualifier)
    {
        return ObjectValuePath.getPath(typeCode, attributeQualifier);
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
