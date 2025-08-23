package com.hybris.backoffice.solrsearch.indexer.cron;

import com.hybris.backoffice.solrsearch.daos.SolrModifiedItemDAO;
import com.hybris.backoffice.solrsearch.model.SolrModifiedItemModel;
import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchConfigService;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.cron.SolrIndexerJob;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

@Deprecated(since = "1808", forRemoval = true)
public abstract class AbstractBackofficeSolrIndexerJob extends SolrIndexerJob
{
    private final Logger LOG = LoggerFactory.getLogger(AbstractBackofficeSolrIndexerJob.class);
    protected SolrModifiedItemDAO solrModifiedItemDAO;
    protected BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;


    public PerformResult performIndexingJob(CronJobModel cronJob)
    {
        Collection<SolrModifiedItemModel> modifiedItemModels = findModifiedItems();
        if(!CollectionUtils.isEmpty(modifiedItemModels))
        {
            synchronizeIndexAndRemoveModifiedItems(modifiedItemModels);
            this.LOG.debug("Solr synchronization completed for {} items", Integer.valueOf(modifiedItemModels.size()));
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    protected void synchronizeIndexAndRemoveModifiedItems(Collection<SolrModifiedItemModel> modifiedItemModels)
    {
        Map<String, List<SolrModifiedItemModel>> itemsByTypecode = (Map<String, List<SolrModifiedItemModel>>)modifiedItemModels.stream().collect(Collectors.groupingBy(SolrModifiedItemModel::getModifiedTypeCode));
        for(Map.Entry<String, List<SolrModifiedItemModel>> entry : itemsByTypecode.entrySet())
        {
            try
            {
                SolrFacetSearchConfigModel solrFacetSearchConfigModel = this.backofficeFacetSearchConfigService.getSolrFacetSearchConfigModel(entry.getKey());
                if(solrFacetSearchConfigModel != null)
                {
                    synchronizeIndexForConfig(solrFacetSearchConfigModel, entry.getValue());
                }
                else
                {
                    this.LOG.warn("Solr facet search config cannot be found for type {}", entry.getKey());
                }
                this.modelService.removeAll(entry.getValue());
            }
            catch(FacetConfigServiceException e)
            {
                this.LOG.warn("Solr facet search config cannot be found for type: " + (String)entry.getKey(), (Throwable)e);
            }
            catch(IndexerException | SolrServiceException e)
            {
                this.LOG.warn("Solr synchronization failed for type: " + (String)entry.getKey(), e);
            }
        }
    }


    protected void synchronizeIndexForConfig(SolrFacetSearchConfigModel config, List<SolrModifiedItemModel> items) throws FacetConfigServiceException, IndexerException, SolrServiceException
    {
        FacetSearchConfig facetSearchConfig = this.facetSearchConfigService.getConfiguration(config.getName());
        Collection<IndexedType> types = facetSearchConfig.getIndexConfig().getIndexedTypes().values();
        for(IndexedType type : types)
        {
            List<PK> pks = (List<PK>)items.stream().map(item -> PK.fromLong(item.getModifiedPk().longValue())).collect(Collectors.toList());
            synchronizeIndexForType(facetSearchConfig, type, pks);
        }
    }


    @Required
    public void setSolrModifiedItemDAO(SolrModifiedItemDAO solrModifiedItemDAO)
    {
        this.solrModifiedItemDAO = solrModifiedItemDAO;
    }


    public SolrModifiedItemDAO getSolrModifiedItemDAO()
    {
        return this.solrModifiedItemDAO;
    }


    @Required
    public void setBackofficeFacetSearchConfigService(BackofficeFacetSearchConfigService backofficeFacetSearchConfigService)
    {
        this.backofficeFacetSearchConfigService = backofficeFacetSearchConfigService;
    }


    public BackofficeFacetSearchConfigService getBackofficeFacetSearchConfigService()
    {
        return this.backofficeFacetSearchConfigService;
    }


    protected abstract void synchronizeIndexForType(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType, Collection<PK> paramCollection) throws IndexerException, SolrServiceException;


    protected abstract Collection<SolrModifiedItemModel> findModifiedItems();
}
