/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview.label;

import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import org.zkoss.zul.Label;

public interface AttributeLabelResolver
{
    Label createAttributeLabel(final Attribute attributeConfiguration, final DataAttribute attribute, final String typeCode);


    String getAttributeLabel(final Attribute attributeConfiguration, final DataAttribute attribute, final String typeCode);
}
