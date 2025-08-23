package de.hybris.platform.solrfacetsearch.indexer.workers.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.workers.IndexerWorker;
import de.hybris.platform.solrfacetsearch.indexer.workers.IndexerWorkerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultIndexerWorkerFactory implements IndexerWorkerFactory, ApplicationContextAware
{
    private String workerBeanId;
    private ApplicationContext applicationContext;


    public String getWorkerBeanId()
    {
        return this.workerBeanId;
    }


    @Required
    public void setWorkerBeanId(String workerBeanId)
    {
        this.workerBeanId = workerBeanId;
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public IndexerWorker createIndexerWorker(FacetSearchConfig facetSearchConfig) throws IndexerException
    {
        try
        {
            return (IndexerWorker)this.applicationContext.getBean(this.workerBeanId, IndexerWorker.class);
        }
        catch(BeansException exception)
        {
            throw new IndexerException("Cannot create indexer worker [" + this.workerBeanId + "]", exception);
        }
    }
}
