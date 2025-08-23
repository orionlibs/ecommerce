/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.quicktogglelocale.controller;

/**
 * IndexedLanguagesResolver checks if language is indexed in the system.
 */
public interface IndexedLanguagesResolver
{
    /**
     * isIndexed checks if language of given isoCode is indexed in the system.
     *
     * @param isoCode
     *           of language
     * @return true if indexed, false otherwise
     */
    boolean isIndexed(final String isoCode);
}
