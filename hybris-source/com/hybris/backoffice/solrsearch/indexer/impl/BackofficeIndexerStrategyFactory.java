package com.hybris.backoffice.solrsearch.indexer.impl;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerStrategy;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexerStrategyFactory;
import de.hybris.platform.solrfacetsearch.indexer.strategies.impl.DefaultIndexerStrategyFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BackofficeIndexerStrategyFactory implements IndexerStrategyFactory, ApplicationContextAware
{
    private DefaultIndexerStrategyFactory defaultIndexerStrategyFactory;
    private ApplicationContext applicationContext;
    private String indexerStrategyBeanName;
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;


    public DefaultIndexerStrategyFactory getDefaultIndexerStrategyFactory()
    {
        return this.defaultIndexerStrategyFactory;
    }


    @Required
    public void setDefaultIndexerStrategyFactory(DefaultIndexerStrategyFactory defaultIndexerStrategyFactory)
    {
        this.defaultIndexerStrategyFactory = defaultIndexerStrategyFactory;
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public String getIndexerStrategyBeanName()
    {
        return this.indexerStrategyBeanName;
    }


    @Required
    public void setIndexerStrategyBeanName(String indexerStrategyBeanName)
    {
        this.indexerStrategyBeanName = indexerStrategyBeanName;
    }


    public BackofficeFacetSearchConfigService getBackofficeFacetSearchConfigService()
    {
        return this.backofficeFacetSearchConfigService;
    }


    @Required
    public void setBackofficeFacetSearchConfigService(BackofficeFacetSearchConfigService backofficeFacetSearchConfigService)
    {
        this.backofficeFacetSearchConfigService = backofficeFacetSearchConfigService;
    }


    public IndexerStrategy createIndexerStrategy(FacetSearchConfig facetSearchConfig) throws IndexerException
    {
        if(this.backofficeFacetSearchConfigService.isValidSearchConfiguredForName(facetSearchConfig.getName()))
        {
            return (IndexerStrategy)this.applicationContext.getBean(getIndexerStrategyBeanName(), IndexerStrategy.class);
        }
        return this.defaultIndexerStrategyFactory.createIndexerStrategy(facetSearchConfig);
    }
}
