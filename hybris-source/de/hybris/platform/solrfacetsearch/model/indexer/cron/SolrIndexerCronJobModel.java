package de.hybris.platform.solrfacetsearch.model.indexer.cron;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import java.util.Map;

public class SolrIndexerCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "SolrIndexerCronJob";
    public static final String _SOLRFACETSEARCHCONFIG2SOLRINDEXERCRONJOB = "SolrFacetSearchConfig2SolrIndexerCronJob";
    public static final String INDEXEROPERATION = "indexerOperation";
    public static final String INDEXERHINTS = "indexerHints";
    public static final String FACETSEARCHCONFIGPOS = "facetSearchConfigPOS";
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";


    public SolrIndexerCronJobModel()
    {
    }


    public SolrIndexerCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerCronJobModel(SolrFacetSearchConfigModel _facetSearchConfig, JobModel _job)
    {
        setFacetSearchConfig(_facetSearchConfig);
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerCronJobModel(SolrFacetSearchConfigModel _facetSearchConfig, JobModel _job, ItemModel _owner)
    {
        setFacetSearchConfig(_facetSearchConfig);
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.GETTER)
    public SolrFacetSearchConfigModel getFacetSearchConfig()
    {
        return (SolrFacetSearchConfigModel)getPersistenceContext().getPropertyValue("facetSearchConfig");
    }


    @Accessor(qualifier = "indexerHints", type = Accessor.Type.GETTER)
    public Map<String, String> getIndexerHints()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("indexerHints");
    }


    @Accessor(qualifier = "indexerOperation", type = Accessor.Type.GETTER)
    public IndexerOperationValues getIndexerOperation()
    {
        return (IndexerOperationValues)getPersistenceContext().getPropertyValue("indexerOperation");
    }


    @Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.SETTER)
    public void setFacetSearchConfig(SolrFacetSearchConfigModel value)
    {
        getPersistenceContext().setPropertyValue("facetSearchConfig", value);
    }


    @Accessor(qualifier = "indexerHints", type = Accessor.Type.SETTER)
    public void setIndexerHints(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("indexerHints", value);
    }


    @Accessor(qualifier = "indexerOperation", type = Accessor.Type.SETTER)
    public void setIndexerOperation(IndexerOperationValues value)
    {
        getPersistenceContext().setPropertyValue("indexerOperation", value);
    }
}
