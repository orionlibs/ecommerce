package com.hybris.backoffice.search.setup;

import com.hybris.backoffice.events.ExternalEventCallback;
import com.hybris.backoffice.search.events.AfterInitializationEndBackofficeSearchListener;
import com.hybris.backoffice.search.services.SearchIndexerJobsService;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;

public class BackofficeSearchStartupHandler
{
    private AfterInitializationEndBackofficeSearchListener afterInitializationEndBackofficeSearchListener;
    private SearchIndexerJobsService searchIndexerJobsService;
    private ExternalEventCallback<AfterInitializationEndEvent> enableSearchJobsCallback;


    public void initialize()
    {
        if(null == this.enableSearchJobsCallback)
        {
            this.enableSearchJobsCallback = createAfterInitializationEndCallback();
        }
        if(null != this.enableSearchJobsCallback)
        {
            registerAfterInitializationEndCallback(this.enableSearchJobsCallback);
        }
    }


    public void destroy()
    {
        this.afterInitializationEndBackofficeSearchListener.unregisterCallback(this.enableSearchJobsCallback);
    }


    public void setAfterInitializationEndBackofficeSearchListener(AfterInitializationEndBackofficeSearchListener afterInitializationEndBackofficeSearchListener)
    {
        this.afterInitializationEndBackofficeSearchListener = afterInitializationEndBackofficeSearchListener;
    }


    public void setSearchIndexerJobsService(SearchIndexerJobsService searchIndexerJobsService)
    {
        this.searchIndexerJobsService = searchIndexerJobsService;
    }


    private ExternalEventCallback<AfterInitializationEndEvent> createAfterInitializationEndCallback()
    {
        return event -> this.searchIndexerJobsService.enableBackofficeSearchIndexerJobs();
    }


    private void registerAfterInitializationEndCallback(ExternalEventCallback<AfterInitializationEndEvent> callback)
    {
        if(!this.afterInitializationEndBackofficeSearchListener.isCallbackRegistered(callback))
        {
            this.afterInitializationEndBackofficeSearchListener.registerCallback(callback);
        }
    }
}
