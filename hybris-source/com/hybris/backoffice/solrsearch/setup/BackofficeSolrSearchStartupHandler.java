package com.hybris.backoffice.solrsearch.setup;

import com.hybris.backoffice.events.ExternalEventCallback;
import com.hybris.backoffice.solrsearch.events.AfterInitializationEndBackofficeSearchListener;
import com.hybris.backoffice.solrsearch.events.AfterInitializationStartBackofficeSearchListener;
import com.hybris.backoffice.solrsearch.services.SolrIndexerJobsService;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class BackofficeSolrSearchStartupHandler
{
    private AfterInitializationEndBackofficeSearchListener afterInitializationEndBackofficeListener;
    private SolrIndexerJobsService solrIndexerJobsService;
    private ExternalEventCallback<AfterInitializationEndEvent> enableSolrJobsCallback;


    public void initialize()
    {
        if(this.enableSolrJobsCallback == null)
        {
            this.enableSolrJobsCallback = createAfterInitializationEndCallback();
        }
        if(this.enableSolrJobsCallback != null)
        {
            registerAfterInitializationEndCallback(this.enableSolrJobsCallback);
        }
    }


    protected ExternalEventCallback<AfterInitializationEndEvent> createAfterInitializationEndCallback()
    {
        return event -> this.solrIndexerJobsService.enableBackofficeSolrSearchIndexerJobs();
    }


    private void registerAfterInitializationEndCallback(ExternalEventCallback<AfterInitializationEndEvent> callback)
    {
        if(!this.afterInitializationEndBackofficeListener.isCallbackRegistered(callback))
        {
            this.afterInitializationEndBackofficeListener.registerCallback(callback);
        }
    }


    public void destroy()
    {
        this.afterInitializationEndBackofficeListener.unregisterCallback(this.enableSolrJobsCallback);
    }


    @Required
    public void setAfterInitializationEndBackofficeListener(AfterInitializationEndBackofficeSearchListener afterInitializationEndBackofficeListener)
    {
        this.afterInitializationEndBackofficeListener = afterInitializationEndBackofficeListener;
    }


    @Deprecated(since = "1811", forRemoval = true)
    public void setAfterInitializationStartBackofficeListener(AfterInitializationStartBackofficeSearchListener afterInitializationStartBackofficeListener)
    {
        throw new UnsupportedOperationException();
    }


    @Required
    public void setSolrIndexerJobsService(SolrIndexerJobsService solrIndexerJobsService)
    {
        this.solrIndexerJobsService = solrIndexerJobsService;
    }
}
