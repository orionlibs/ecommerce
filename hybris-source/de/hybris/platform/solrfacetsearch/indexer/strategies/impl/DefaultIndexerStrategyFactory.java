package de.hybris.platform.solrfacetsearch.indexer.strategies.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerStrategy;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerStrategyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultIndexerStrategyFactory implements IndexerStrategyFactory, ApplicationContextAware
{
    private String indexerStrategyBeanId;
    private String distributedIndexerStrategyBeanId;
    private ApplicationContext applicationContext;


    public String getIndexerStrategyBeanId()
    {
        return this.indexerStrategyBeanId;
    }


    @Required
    public void setIndexerStrategyBeanId(String indexerStrategyBeanId)
    {
        this.indexerStrategyBeanId = indexerStrategyBeanId;
    }


    public String getDistributedIndexerStrategyBeanId()
    {
        return this.distributedIndexerStrategyBeanId;
    }


    @Required
    public void setDistributedIndexerStrategyBeanId(String distributedIndexerStrategyBeanId)
    {
        this.distributedIndexerStrategyBeanId = distributedIndexerStrategyBeanId;
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public IndexerStrategy createIndexerStrategy(FacetSearchConfig facetSearchConfig) throws IndexerException
    {
        try
        {
            if(facetSearchConfig.getIndexConfig().isDistributedIndexing())
            {
                return (IndexerStrategy)this.applicationContext.getBean(this.distributedIndexerStrategyBeanId, IndexerStrategy.class);
            }
            return (IndexerStrategy)this.applicationContext.getBean(this.indexerStrategyBeanId, IndexerStrategy.class);
        }
        catch(BeansException e)
        {
            throw new IndexerException("Cannot create indexer strategy [" + this.indexerStrategyBeanId + "]", e);
        }
    }
}
