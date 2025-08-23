/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.selectivesync.tree;

/** Listener for changes in selective synchronization tree's filter. */
@FunctionalInterface
public interface FilterExecutionListener
{
    /** Fired when filter has changed. */
    void onFilterExecuted();
}
