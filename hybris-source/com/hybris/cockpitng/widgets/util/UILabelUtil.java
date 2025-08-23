/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.util;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.labels.LabelUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;

public class UILabelUtil
{
    private UILabelUtil()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    public static String getColumnHeaderLabel(final ListColumn column, final String typeCode, final LabelService labelService)
    {
        final String label = column.getLabel();
        if(label == null)
        {
            return extractFromTypeCodeAndQualifier(column, typeCode, labelService);
        }
        else if(StringUtils.isNotBlank(label))
        {
            return Labels.getLabel(label, LabelUtils.getFallbackLabel(label));
        }
        else
        {
            return StringUtils.EMPTY;
        }
    }


    private static String extractFromTypeCodeAndQualifier(final ListColumn column, final String typeCode,
                    final LabelService labelService)
    {
        final boolean isTypeCodeValid = StringUtils.isNotBlank(typeCode) && typeCode.indexOf('.') == -1;
        final boolean isQualifierValid = StringUtils.isNotBlank(column.getQualifier());
        if(isTypeCodeValid && isQualifierValid)
        {
            final String locAttributeLabel = labelService.getObjectLabel(typeCode + '.' + column.getQualifier());
            if(StringUtils.isNotBlank(locAttributeLabel))
            {
                return locAttributeLabel;
            }
        }
        return column.getQualifier();
    }


    /**
     * Returns localized label by key or a "[labelKey]" if localized label was not registered
     */
    public static String resolveLocalizedLabel(final String labelKey)
    {
        final String defaultValue = LabelUtils.getFallbackLabel(labelKey);
        return Labels.getLabel(labelKey, defaultValue);
    }
}
