/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.bulkedit;

import java.util.Collection;

/**
 * Dynamic attribute handler for bulk edit.
 */
public interface DynamicAttributeBulkEditHandler
{
    /**
     * Returns true, if this handler can handle the given qualifier.
     */
    boolean canHandle(final String qualifier);


    /**
     * Returns the selected items.
     */
    Collection getSelectedItems();
}
