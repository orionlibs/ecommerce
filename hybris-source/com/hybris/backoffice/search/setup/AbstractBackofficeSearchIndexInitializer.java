/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.search.setup;

import com.hybris.backoffice.search.events.AfterInitializationEndBackofficeSearchListener;
import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;

public abstract class AbstractBackofficeSearchIndexInitializer
{
    protected BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    protected AfterInitializationEndBackofficeSearchListener afterInitializationEndBackofficeListener;


    public void initialize()
    {
        this.initializeIndexesIfNecessary();
        registerSystemInitializationEndCallback();
    }


    protected abstract void initializeIndexesIfNecessary();


    protected abstract boolean shouldInitializeIndexes();


    public void setBackofficeFacetSearchConfigService(BackofficeFacetSearchConfigService backofficeFacetSearchConfigService)
    {
        this.backofficeFacetSearchConfigService = backofficeFacetSearchConfigService;
    }


    public void setAfterInitializationEndBackofficeListener(AfterInitializationEndBackofficeSearchListener afterInitializationEndBackofficeListener)
    {
        this.afterInitializationEndBackofficeListener = afterInitializationEndBackofficeListener;
    }


    protected void registerSystemInitializationEndCallback()
    {
        if(!afterInitializationEndBackofficeListener.isCallbackRegistered(this::handleSystemInitializationEndEvent))
        {
            afterInitializationEndBackofficeListener.registerCallback(this::handleSystemInitializationEndEvent);
        }
    }


    protected void handleSystemInitializationEndEvent(final AfterInitializationEndEvent event)
    {
        initializeIndexesIfNecessary();
    }
}
