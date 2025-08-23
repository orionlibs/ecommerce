package de.hybris.platform.solrfacetsearch.indexer.cron;

import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.indexer.IndexerQueriesExecutor;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerJobException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrExtIndexerCronJobModel;
import de.hybris.platform.solrfacetsearch.provider.ContextAwareParameterProvider;
import de.hybris.platform.solrfacetsearch.provider.CronJobAwareParameterProvider;
import de.hybris.platform.solrfacetsearch.provider.ParameterProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Required;

public class SolrExtIndexerJob<T extends SolrExtIndexerCronJobModel> extends AbstractJobPerformable<T> implements BeanFactoryAware
{
    private static final Logger LOG = Logger.getLogger(SolrExtIndexerJob.class);
    private FacetSearchConfigService facetSearchConfigService;
    private IndexerService indexerService;
    private IndexerQueriesExecutor indexerQueriesExecutor;
    private BeanFactory beanFactory;


    @Required
    public void setFacetSearchConfigService(FacetSearchConfigService facetSearchConfigService)
    {
        this.facetSearchConfigService = facetSearchConfigService;
    }


    public FacetSearchConfigService getFacetSearchConfigService()
    {
        return this.facetSearchConfigService;
    }


    @Required
    public void setIndexerService(IndexerService indexerService)
    {
        this.indexerService = indexerService;
    }


    public IndexerService getIndexerService()
    {
        return this.indexerService;
    }


    @Required
    public void setIndexerQueriesExecutor(IndexerQueriesExecutor indexerQueriesExecutor)
    {
        this.indexerQueriesExecutor = indexerQueriesExecutor;
    }


    public IndexerQueriesExecutor getIndexerQueriesExecutor()
    {
        return this.indexerQueriesExecutor;
    }


    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }


    protected BeanFactory getBeanFactory()
    {
        return this.beanFactory;
    }


    public PerformResult perform(T cronJob)
    {
        LOG.info("Started ext indexer cronjob.");
        SolrFacetSearchConfigModel facetSearchConfigModel = cronJob.getFacetSearchConfig();
        String facetSearchConfigName = facetSearchConfigModel.getName();
        try
        {
            validateCronJobParameters(cronJob);
            FacetSearchConfig facetSearchConfig = this.facetSearchConfigService.getConfiguration(facetSearchConfigName);
            IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
            IndexedType indexedType = resolveIndexedType(cronJob, indexConfig);
            List<IndexedProperty> indexedProperties = resolveIndexedProperties(cronJob, indexedType);
            String query = cronJob.getQuery();
            Map<String, Object> queryParameters = createQueryParameters(cronJob, indexConfig, indexedType);
            List<PK> pks = this.indexerQueriesExecutor.getPks(facetSearchConfig, indexedType, query, queryParameters);
            Map<String, String> indexerHints = cronJob.getIndexerHints();
            performIndexing(cronJob, facetSearchConfig, indexedType, indexedProperties, pks, indexerHints);
        }
        catch(FacetConfigServiceException e)
        {
            LOG.error("Error loading configuration with name: " + facetSearchConfigName, (Throwable)e);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
        }
        catch(IndexerJobException | IndexerException e)
        {
            LOG.error("Error running indexer job for configuration with name: " + facetSearchConfigName, e);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    protected void validateCronJobParameters(T cronJob) throws IndexerJobException
    {
        if(StringUtils.isBlank(cronJob.getIndexedType()))
        {
            throw new IndexerJobException("Indexed type must be defined");
        }
        if(StringUtils.isBlank(cronJob.getQuery()))
        {
            throw new IndexerJobException("Query must be defined");
        }
    }


    protected IndexedType resolveIndexedType(T cronJob, IndexConfig indexConfig) throws IndexerJobException
    {
        String indexedTypeName = cronJob.getIndexedType();
        IndexedType indexedType = null;
        if(indexConfig.getIndexedTypes() != null)
        {
            indexedType = (IndexedType)indexConfig.getIndexedTypes().get(indexedTypeName);
        }
        if(indexedType == null)
        {
            throw new IndexerJobException("Indexed type " + indexedTypeName + " not found");
        }
        return indexedType;
    }


    protected List<IndexedProperty> resolveIndexedProperties(T cronJob, IndexedType indexedType) throws IndexerJobException
    {
        List<IndexedProperty> indexedProperties = new ArrayList<>();
        Collection<String> indexedPropertiesNames = cronJob.getIndexedProperties();
        if(indexedPropertiesNames != null)
        {
            for(String indexedPropertyName : indexedPropertiesNames)
            {
                IndexedProperty indexedProperty = null;
                if(indexedType.getIndexedProperties() != null)
                {
                    indexedProperty = (IndexedProperty)indexedType.getIndexedProperties().get(indexedPropertyName);
                }
                if(indexedProperty == null)
                {
                    throw new IndexerJobException("Indexed property " + indexedPropertyName + " not found");
                }
                indexedProperties.add(indexedProperty);
            }
        }
        return indexedProperties;
    }


    protected Map<String, Object> createQueryParameters(T cronJob, IndexConfig indexConfig, IndexedType indexedType) throws IndexerJobException
    {
        Map<String, Object> parameters = new HashMap<>();
        String parameterProviderId = cronJob.getQueryParameterProvider();
        if(!StringUtils.isBlank(parameterProviderId))
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
                if(parameterProvider instanceof CronJobAwareParameterProvider)
                {
                    parameters.putAll(((CronJobAwareParameterProvider)parameterProvider)
                                    .createParameters((CronJobModel)cronJob, indexConfig, indexedType));
                }
            }
            catch(NoSuchBeanDefinitionException e)
            {
                throw new IndexerJobException("Could not instantiate parameter provider with id: " + parameterProviderId, e);
            }
        }
        return parameters;
    }


    protected void performIndexing(T cronJob, FacetSearchConfig facetSearchConfig, IndexedType indexedType, List<IndexedProperty> indexedProperties, List<PK> pks, Map<String, String> indexerHints) throws IndexerJobException, IndexerException
    {
        IndexerOperationValues indexerOperation = cronJob.getIndexerOperation();
        switch(null.$SwitchMap$de$hybris$platform$solrfacetsearch$enums$IndexerOperationValues[indexerOperation.ordinal()])
        {
            case 1:
                this.indexerService.updateTypeIndex(facetSearchConfig, indexedType, pks, indexerHints);
                return;
            case 2:
                this.indexerService.updatePartialTypeIndex(facetSearchConfig, indexedType, indexedProperties, pks, indexerHints);
                return;
            case 3:
                this.indexerService.deleteTypeIndex(facetSearchConfig, indexedType, pks, indexerHints);
                return;
        }
        throw new IndexerJobException("Unsupported indexer operation: " + indexerOperation);
    }
}
