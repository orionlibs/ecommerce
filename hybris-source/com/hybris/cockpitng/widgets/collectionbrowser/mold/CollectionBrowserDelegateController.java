/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold;

import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;

/**
 * Contains delegated part of the Collection Browser logic.
 */
public interface CollectionBrowserDelegateController
{
    /**
     * Sets Collection Browser controller
     * @param controller Collection Browser controller used in delegate
     */
    void setController(final CollectionBrowserController controller);
}
