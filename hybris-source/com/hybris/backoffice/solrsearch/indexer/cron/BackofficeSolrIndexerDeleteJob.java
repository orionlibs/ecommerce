package com.hybris.backoffice.solrsearch.indexer.cron;

import com.google.common.collect.Lists;
import com.hybris.backoffice.solrsearch.enums.SolrItemModificationType;
import com.hybris.backoffice.solrsearch.model.SolrModifiedItemModel;
import com.hybris.backoffice.solrsearch.utils.SolrPlatformUtils;
import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContextFactory;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.spi.Exporter;
import de.hybris.platform.solrfacetsearch.indexer.strategies.IndexOperationIdGenerator;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "1808", forRemoval = true)
public class BackofficeSolrIndexerDeleteJob extends AbstractBackofficeSolrIndexerJob implements BeanFactoryAware
{
    private final Logger LOGGER = LoggerFactory.getLogger(BackofficeSolrIndexerDeleteJob.class);
    protected BeanFactory beanFactory;
    protected IndexerBatchContextFactory indexerBatchContextFactory;
    protected IndexOperationIdGenerator indexOperationIdGenerator;
    protected SolrSearchProviderFactory solrSearchProviderFactory;
    protected SolrIndexService solrIndexService;


    protected void synchronizeIndexForType(FacetSearchConfig facetSearchConfig, IndexedType type, Collection<PK> pks) throws IndexerException, SolrServiceException
    {
        IndexerBatchContext context = null;
        try
        {
            SolrIndexModel activeIndex = this.solrIndexService.getActiveIndex(facetSearchConfig.getName(), type.getIdentifier());
            SolrSearchProvider searchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, type);
            Index index = searchProvider.resolveIndex(facetSearchConfig, type, activeIndex.getQualifier());
            long indexOperationId = this.indexOperationIdGenerator.generate(facetSearchConfig, type, index);
            context = this.indexerBatchContextFactory.createContext(indexOperationId, IndexOperation.DELETE, true, facetSearchConfig, type,
                            Collections.emptyList());
            context.setIndex(index);
            SolrConfig solrConfig = facetSearchConfig.getSolrConfig();
            Exporter exporter = (Exporter)this.beanFactory.getBean(
                            SolrPlatformUtils.createSolrExporterBeanName(solrConfig.getMode()), Exporter.class);
            List<String> pksString = (List<String>)pks.stream().map(PK::getLongValueAsString).collect(Collectors.toList());
            this.indexerBatchContextFactory.prepareContext();
            context.setItems(Lists.newArrayList());
            this.indexerBatchContextFactory.initializeContext();
            exporter.exportToDeleteFromIndex(pksString, facetSearchConfig, type);
            this.indexerBatchContextFactory.destroyContext();
        }
        catch(BeansException e)
        {
            this.LOGGER.warn("Solr exporter bean not found " + facetSearchConfig.getName(), (Throwable)e);
            if(context != null)
            {
                this.indexerBatchContextFactory.destroyContext((Exception)e);
            }
        }
        catch(SolrServiceException | IndexerException | RuntimeException e)
        {
            if(context != null)
            {
                this.indexerBatchContextFactory.destroyContext(e);
            }
            throw e;
        }
    }


    protected Collection<SolrModifiedItemModel> findModifiedItems()
    {
        return this.solrModifiedItemDAO.findByModificationType(SolrItemModificationType.DELETE);
    }


    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }


    @Required
    public void setIndexerBatchContextFactory(IndexerBatchContextFactory indexerBatchContextFactory)
    {
        this.indexerBatchContextFactory = indexerBatchContextFactory;
    }


    @Required
    public void setSolrSearchProviderFactory(SolrSearchProviderFactory solrSearchProviderFactory)
    {
        this.solrSearchProviderFactory = solrSearchProviderFactory;
    }


    @Required
    public void setIndexOperationIdGenerator(IndexOperationIdGenerator indexOperationIdGenerator)
    {
        this.indexOperationIdGenerator = indexOperationIdGenerator;
    }


    @Required
    public void setSolrIndexService(SolrIndexService solrIndexService)
    {
        this.solrIndexService = solrIndexService;
    }
}
