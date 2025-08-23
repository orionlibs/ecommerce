package de.hybris.platform.solrfacetsearch.config.factories.impl;

import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FlexibleSearchQuerySpec;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeFlexibleSearchQuery;
import de.hybris.platform.solrfacetsearch.config.exceptions.ParameterProviderException;
import de.hybris.platform.solrfacetsearch.config.factories.FlexibleSearchQuerySpecFactory;
import de.hybris.platform.solrfacetsearch.config.impl.IndexTypeFSQSpec;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.provider.ContextAwareParameterProvider;
import de.hybris.platform.solrfacetsearch.provider.ParameterProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexOperationService;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrIndexNotFoundException;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFlexibleSearchQuerySpecFactory implements FlexibleSearchQuerySpecFactory, BeanFactoryAware
{
    private static final Logger LOG = Logger.getLogger(DefaultFlexibleSearchQuerySpecFactory.class);
    protected static final String CURRENTDATE = "currentDate";
    protected static final String CURRENTTIME = "currentTime";
    protected static final String LASTINDEXTIME = "lastIndexTime";
    private SolrIndexService indexService;
    private SolrIndexOperationService indexOperationService;
    private TimeService timeService;
    private BeanFactory beanFactory;


    public FlexibleSearchQuerySpec createIndexQuery(IndexedTypeFlexibleSearchQuery indexTypeFlexibleSearchQueryData, IndexedType indexedType, FacetSearchConfig facetSearchConfig) throws SolrServiceException
    {
        populateRuntimeParameters(indexTypeFlexibleSearchQueryData, indexedType, facetSearchConfig);
        return (FlexibleSearchQuerySpec)new IndexTypeFSQSpec(indexTypeFlexibleSearchQueryData);
    }


    protected void populateRuntimeParameters(IndexedTypeFlexibleSearchQuery indexTypeFlexibleSearchQueryData, IndexedType indexedType, FacetSearchConfig facetSearchConfig) throws SolrServiceException
    {
        Map<String, Object> parameters = indexTypeFlexibleSearchQueryData.getParameters();
        if(parameters == null)
        {
            indexTypeFlexibleSearchQueryData.setParameters(new HashMap<>());
            parameters = indexTypeFlexibleSearchQueryData.getParameters();
        }
        if(indexTypeFlexibleSearchQueryData.isInjectLastIndexTime())
        {
            parameters.put("lastIndexTime", getLastIndexTime(facetSearchConfig, indexedType));
        }
        if(indexTypeFlexibleSearchQueryData.isInjectCurrentTime())
        {
            parameters.put("currentTime", getCurrentTime());
        }
        if(indexTypeFlexibleSearchQueryData.isInjectCurrentDate())
        {
            parameters.put("currentDate", getCurrentDate());
        }
        if(StringUtils.isNotEmpty(indexTypeFlexibleSearchQueryData.getParameterProviderId()))
        {
            populateParametersByProvider(indexTypeFlexibleSearchQueryData.getParameterProviderId(), parameters, indexedType, facetSearchConfig
                            .getIndexConfig());
        }
    }


    protected void populateParametersByProvider(String parameterProviderId, Map<String, Object> parameters, IndexedType indexedType, IndexConfig indexConfig)
    {
        try
        {
            Object parameterProvider = this.beanFactory.getBean(parameterProviderId);
            if(parameterProvider instanceof ParameterProvider)
            {
                parameters.putAll(((ParameterProvider)parameterProvider).createParameters());
            }
            if(parameterProvider instanceof ContextAwareParameterProvider)
            {
                parameters.putAll(((ContextAwareParameterProvider)parameterProvider).createParameters(indexConfig, indexedType));
            }
        }
        catch(NoSuchBeanDefinitionException e)
        {
            throw new ParameterProviderException("Could not create flexible search query parameters by [" + parameterProviderId + "] provider", e);
        }
        catch(RuntimeException e)
        {
            LOG.error("Error creating FSQ parameters using provider : [" + parameterProviderId + "]", e);
        }
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }


    protected Date getLastIndexTime(FacetSearchConfig facetSearchConfig, IndexedType indexedType) throws SolrServiceException
    {
        Date lastIndexTime;
        try
        {
            SolrIndexModel activeIndex = this.indexService.getActiveIndex(facetSearchConfig.getName(), indexedType.getIdentifier());
            lastIndexTime = this.indexOperationService.getLastIndexOperationTime(activeIndex);
        }
        catch(SolrIndexNotFoundException e)
        {
            LOG.debug(e);
            lastIndexTime = new Date(0L);
        }
        return lastIndexTime;
    }


    protected Date getCurrentDate()
    {
        Calendar now = Calendar.getInstance();
        now.setTime(getCurrentTime());
        now.set(14, 0);
        now.set(13, 0);
        now.set(12, 0);
        now.set(10, 0);
        now.set(11, 0);
        return now.getTime();
    }


    protected Date getCurrentTime()
    {
        return this.timeService.getCurrentTime();
    }


    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }


    public SolrIndexService getIndexService()
    {
        return this.indexService;
    }


    @Required
    public void setIndexService(SolrIndexService indexService)
    {
        this.indexService = indexService;
    }


    @Required
    public void setIndexOperationService(SolrIndexOperationService indexOperationService)
    {
        this.indexOperationService = indexOperationService;
    }


    public SolrIndexOperationService getIndexOperationService()
    {
        return this.indexOperationService;
    }
}
