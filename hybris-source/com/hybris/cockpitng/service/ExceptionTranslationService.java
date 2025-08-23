/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.service;

/**
 * Extension for {@link ExceptionTranslationHandler}, with default implementation of {@link #canHandle(Throwable)}
 * always return true
 */
public interface ExceptionTranslationService extends ExceptionTranslationHandler
{
    @Override
    default boolean canHandle(final Throwable exception)
    {
        return true;
    }
}
