package de.hybris.platform.solrfacetsearch.model.indexer.cron;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import java.util.Collection;

public class SolrIndexerHotUpdateCronJobModel extends SolrIndexerCronJobModel
{
    public static final String _TYPECODE = "SolrIndexerHotUpdateCronJob";
    public static final String INDEXTYPENAME = "indexTypeName";
    public static final String ITEMS = "items";


    public SolrIndexerHotUpdateCronJobModel()
    {
    }


    public SolrIndexerHotUpdateCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerHotUpdateCronJobModel(SolrFacetSearchConfigModel _facetSearchConfig, String _indexTypeName, JobModel _job)
    {
        setFacetSearchConfig(_facetSearchConfig);
        setIndexTypeName(_indexTypeName);
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexerHotUpdateCronJobModel(SolrFacetSearchConfigModel _facetSearchConfig, String _indexTypeName, JobModel _job, ItemModel _owner)
    {
        setFacetSearchConfig(_facetSearchConfig);
        setIndexTypeName(_indexTypeName);
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "indexTypeName", type = Accessor.Type.GETTER)
    public String getIndexTypeName()
    {
        return (String)getPersistenceContext().getPropertyValue("indexTypeName");
    }


    @Accessor(qualifier = "items", type = Accessor.Type.GETTER)
    public Collection<ItemModel> getItems()
    {
        return (Collection<ItemModel>)getPersistenceContext().getPropertyValue("items");
    }


    @Accessor(qualifier = "indexTypeName", type = Accessor.Type.SETTER)
    public void setIndexTypeName(String value)
    {
        getPersistenceContext().setPropertyValue("indexTypeName", value);
    }


    @Accessor(qualifier = "items", type = Accessor.Type.SETTER)
    public void setItems(Collection<ItemModel> value)
    {
        getPersistenceContext().setPropertyValue("items", value);
    }
}
