/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.labels.impl;

import com.hybris.backoffice.proxy.LabelServiceProxy;
import com.hybris.cockpitng.labels.LabelService;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.Locales;

public class BackofficeLabelServiceProxy implements LabelServiceProxy
{
    private LabelService labelService;


    @Override
    public String getObjectLabel(final Object object, final Locale locale)
    {
        final Locale currentLocal = Locales.getCurrent();
        Optional.ofNullable(locale).ifPresent(Locales::setThreadLocal);
        final String objectLabel = getObjectLabel(object);
        Locales.setThreadLocal(currentLocal);
        return objectLabel;
    }


    public String getObjectLabel(final Object object)
    {
        return labelService.getObjectLabel(object);
    }


    @Override
    public String getObjectDescription(final Object object)
    {
        return labelService.getObjectDescription(object);
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}
