/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.service;

public interface ExceptionTranslationHandler
{
    boolean canHandle(final Throwable exception);


    String toString(final Throwable exception);
}
