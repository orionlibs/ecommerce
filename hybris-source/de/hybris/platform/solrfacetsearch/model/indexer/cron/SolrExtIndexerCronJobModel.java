package de.hybris.platform.solrfacetsearch.model.indexer.cron;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import java.util.Collection;

public class SolrExtIndexerCronJobModel extends SolrIndexerCronJobModel
{
    public static final String _TYPECODE = "SolrExtIndexerCronJob";
    public static final String INDEXEDTYPE = "indexedType";
    public static final String INDEXEDPROPERTIES = "indexedProperties";
    public static final String QUERY = "query";
    public static final String QUERYPARAMETERPROVIDER = "queryParameterProvider";


    public SolrExtIndexerCronJobModel()
    {
    }


    public SolrExtIndexerCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrExtIndexerCronJobModel(SolrFacetSearchConfigModel _facetSearchConfig, String _indexedType, JobModel _job, String _query)
    {
        setFacetSearchConfig(_facetSearchConfig);
        setIndexedType(_indexedType);
        setJob(_job);
        setQuery(_query);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrExtIndexerCronJobModel(SolrFacetSearchConfigModel _facetSearchConfig, String _indexedType, JobModel _job, ItemModel _owner, String _query)
    {
        setFacetSearchConfig(_facetSearchConfig);
        setIndexedType(_indexedType);
        setJob(_job);
        setOwner(_owner);
        setQuery(_query);
    }


    @Accessor(qualifier = "indexedProperties", type = Accessor.Type.GETTER)
    public Collection<String> getIndexedProperties()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("indexedProperties");
    }


    @Accessor(qualifier = "indexedType", type = Accessor.Type.GETTER)
    public String getIndexedType()
    {
        return (String)getPersistenceContext().getPropertyValue("indexedType");
    }


    @Accessor(qualifier = "query", type = Accessor.Type.GETTER)
    public String getQuery()
    {
        return (String)getPersistenceContext().getPropertyValue("query");
    }


    @Accessor(qualifier = "queryParameterProvider", type = Accessor.Type.GETTER)
    public String getQueryParameterProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("queryParameterProvider");
    }


    @Accessor(qualifier = "indexedProperties", type = Accessor.Type.SETTER)
    public void setIndexedProperties(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("indexedProperties", value);
    }


    @Accessor(qualifier = "indexedType", type = Accessor.Type.SETTER)
    public void setIndexedType(String value)
    {
        getPersistenceContext().setPropertyValue("indexedType", value);
    }


    @Accessor(qualifier = "query", type = Accessor.Type.SETTER)
    public void setQuery(String value)
    {
        getPersistenceContext().setPropertyValue("query", value);
    }


    @Accessor(qualifier = "queryParameterProvider", type = Accessor.Type.SETTER)
    public void setQueryParameterProvider(String value)
    {
        getPersistenceContext().setPropertyValue("queryParameterProvider", value);
    }
}
