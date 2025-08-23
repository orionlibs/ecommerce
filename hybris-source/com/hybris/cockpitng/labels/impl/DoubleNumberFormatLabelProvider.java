/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.labels.impl;

import com.hybris.cockpitng.labels.LabelProvider;
import java.text.NumberFormat;
import org.apache.commons.lang3.StringUtils;

public class DoubleNumberFormatLabelProvider implements LabelProvider<Double>
{
    @Override
    public String getLabel(final Double object)
    {
        if(object == null)
        {
            return StringUtils.EMPTY;
        }
        return NumberFormat.getInstance().format(object.doubleValue());
    }


    @Override
    public String getDescription(final Double object)
    {
        return null;
    }


    @Override
    public String getIconPath(final Double object)
    {
        return null;
    }
}
