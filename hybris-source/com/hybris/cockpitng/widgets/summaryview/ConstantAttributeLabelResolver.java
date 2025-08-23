/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview;

import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;

public class ConstantAttributeLabelResolver extends AbstractAttributeLabelResolver
{
    private String labelKey;
    private String label;


    @Override
    public String getAttributeLabel(final Attribute attributeConfiguration, final DataAttribute attribute, final String typeCode)
    {
        if(StringUtils.isNotEmpty(getLabelKey()))
        {
            return Labels.getLabel(getLabelKey());
        }
        else
        {
            return ObjectUtils.defaultIfNull(getLabel(), StringUtils.EMPTY);
        }
    }


    protected String getLabelKey()
    {
        return labelKey;
    }


    public void setLabelKey(final String labelKey)
    {
        this.labelKey = labelKey;
    }


    protected String getLabel()
    {
        return label;
    }


    public void setLabel(final String label)
    {
        this.label = label;
    }
}
