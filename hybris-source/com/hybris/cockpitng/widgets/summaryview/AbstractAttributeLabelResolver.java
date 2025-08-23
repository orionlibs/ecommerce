/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview;

import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.widgets.summaryview.label.AttributeLabelResolver;
import org.zkoss.zul.Label;

public abstract class AbstractAttributeLabelResolver implements AttributeLabelResolver
{
    protected static final String SCLASS_ATTRIBUTE_LABEL = "yw-summaryview-attribute-label";


    @Override
    public Label createAttributeLabel(final Attribute attributeConfiguration, final DataAttribute attribute, final String typeCode)
    {
        final Label attributeLabel = new Label(getAttributeLabel(attributeConfiguration, attribute, typeCode));
        if(attribute != null)
        {
            attributeLabel.setTooltiptext(attribute.getQualifier());
        }
        attributeLabel.setSclass(SCLASS_ATTRIBUTE_LABEL);
        return attributeLabel;
    }
}
