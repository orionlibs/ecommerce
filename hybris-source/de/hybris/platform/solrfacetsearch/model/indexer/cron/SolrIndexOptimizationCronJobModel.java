package de.hybris.platform.solrfacetsearch.model.indexer.cron;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

public class SolrIndexOptimizationCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "SolrIndexOptimizationCronJob";
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";


    public SolrIndexOptimizationCronJobModel()
    {
    }


    public SolrIndexOptimizationCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexOptimizationCronJobModel(SolrFacetSearchConfigModel _facetSearchConfig, JobModel _job)
    {
        setFacetSearchConfig(_facetSearchConfig);
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexOptimizationCronJobModel(SolrFacetSearchConfigModel _facetSearchConfig, JobModel _job, ItemModel _owner)
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


    @Accessor(qualifier = "facetSearchConfig", type = Accessor.Type.SETTER)
    public void setFacetSearchConfig(SolrFacetSearchConfigModel value)
    {
        getPersistenceContext().setPropertyValue("facetSearchConfig", value);
    }
}
