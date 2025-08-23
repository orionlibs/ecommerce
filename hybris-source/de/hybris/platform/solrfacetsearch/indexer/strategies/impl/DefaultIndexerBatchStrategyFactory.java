package de.hybris.platform.solrfacetsearch.indexer.strategies.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerBatchStrategy;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerBatchStrategyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultIndexerBatchStrategyFactory implements IndexerBatchStrategyFactory, ApplicationContextAware
{
    private String indexerBatchStrategyBeanId;
    private ApplicationContext applicationContext;


    public IndexerBatchStrategy createIndexerBatchStrategy(FacetSearchConfig facetSearchConfig) throws IndexerException
    {
        try
        {
            return (IndexerBatchStrategy)this.applicationContext.getBean(this.indexerBatchStrategyBeanId, IndexerBatchStrategy.class);
        }
        catch(BeansException e)
        {
            throw new IndexerException("Cannot create indexer batch strategy [" + this.indexerBatchStrategyBeanId + "]", e);
        }
    }


    public String getIndexerBatchStrategyBeanId()
    {
        return this.indexerBatchStrategyBeanId;
    }


    @Required
    public void setIndexerBatchStrategyBeanId(String indexerBatchStrategyBeanId)
    {
        this.indexerBatchStrategyBeanId = indexerBatchStrategyBeanId;
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
