/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.service.impl;

import com.hybris.cockpitng.service.ExceptionTranslationHandler;
import com.hybris.cockpitng.service.ExceptionTranslationService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link ExceptionTranslationService}, {@link #toString(Throwable)} return message handled by
 * first handler which can handle or by default handler.
 */
public class DefaultExceptionTranslationService implements ExceptionTranslationService
{
    private Collection<ExceptionTranslationHandler> exceptionHandlers;
    private ExceptionTranslationHandler defaultHandler;


    @Override
    public String toString(final Throwable exception)
    {
        if(getExceptionHandlers() != null)
        {
            for(final ExceptionTranslationHandler exceptionTranslationHandler : getExceptionHandlers())
            {
                if(exceptionTranslationHandler.canHandle(exception))
                {
                    return exceptionTranslationHandler.toString(exception);
                }
            }
        }
        return getDefaultHandler().toString(exception);
    }


    public ExceptionTranslationHandler getDefaultHandler()
    {
        return defaultHandler;
    }


    @Required
    public void setDefaultHandler(final ExceptionTranslationHandler defaultHandler)
    {
        this.defaultHandler = defaultHandler;
    }


    public Collection<ExceptionTranslationHandler> getExceptionHandlers()
    {
        return exceptionHandlers;
    }


    public void setExceptionHandlers(final Collection<ExceptionTranslationHandler> exceptionHandlers)
    {
        this.exceptionHandlers = exceptionHandlers;
    }
}
