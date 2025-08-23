/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.quicktogglelocale.controller;

/**
 * Default implementation of {@link IndexedLanguagesResolver}.
 */
public class DefaultIndexedLanguagesResolver implements IndexedLanguagesResolver
{
    @Override
    public boolean isIndexed(final String isoCode)
    {
        return true;
    }
}
