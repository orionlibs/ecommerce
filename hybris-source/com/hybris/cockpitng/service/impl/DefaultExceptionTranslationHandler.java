/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.service.impl;

import com.hybris.cockpitng.annotations.LocalizedException;
import com.hybris.cockpitng.service.ExceptionTranslationHandler;
import org.zkoss.util.resource.Labels;

public class DefaultExceptionTranslationHandler implements ExceptionTranslationHandler
{
    public static final String I18N_GENERAL_UNEXPECTED_FAILURE = "general.unexpected.failure";


    @Override
    public boolean canHandle(final Throwable exception)
    {
        return exception != null;
    }


    @Override
    public String toString(final Throwable exception)
    {
        if(exception.getClass().isAnnotationPresent(LocalizedException.class))
        {
            return exception.getLocalizedMessage();
        }
        return getLabelByKey(I18N_GENERAL_UNEXPECTED_FAILURE);
    }


    String getLabelByKey(final String key)
    {
        return Labels.getLabel(key);
    }
}
