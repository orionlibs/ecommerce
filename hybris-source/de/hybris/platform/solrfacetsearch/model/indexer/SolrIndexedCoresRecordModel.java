package de.hybris.platform.solrfacetsearch.model.indexer;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.SolrServerModes;
import de.hybris.platform.solrfacetsearch.model.SolrIndexOperationRecordModel;
import java.util.Collection;
import java.util.Date;

public class SolrIndexedCoresRecordModel extends ItemModel
{
    public static final String _TYPECODE = "SolrIndexedCoresRecord";
    public static final String CORENAME = "coreName";
    public static final String INDEXNAME = "indexName";
    public static final String INDEXTIME = "indexTime";
    public static final String CURRENTINDEXDATASUBDIRECTORY = "currentIndexDataSubDirectory";
    public static final String SERVERMODE = "serverMode";
    public static final String INDEXOPERATIONS = "indexOperations";


    public SolrIndexedCoresRecordModel()
    {
    }


    public SolrIndexedCoresRecordModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexedCoresRecordModel(String _coreName, String _indexName)
    {
        setCoreName(_coreName);
        setIndexName(_indexName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexedCoresRecordModel(String _coreName, String _indexName, ItemModel _owner)
    {
        setCoreName(_coreName);
        setIndexName(_indexName);
        setOwner(_owner);
    }


    @Accessor(qualifier = "coreName", type = Accessor.Type.GETTER)
    public String getCoreName()
    {
        return (String)getPersistenceContext().getPropertyValue("coreName");
    }


    @Accessor(qualifier = "currentIndexDataSubDirectory", type = Accessor.Type.GETTER)
    public String getCurrentIndexDataSubDirectory()
    {
        return (String)getPersistenceContext().getPropertyValue("currentIndexDataSubDirectory");
    }


    @Accessor(qualifier = "indexName", type = Accessor.Type.GETTER)
    public String getIndexName()
    {
        return (String)getPersistenceContext().getPropertyValue("indexName");
    }


    @Accessor(qualifier = "indexOperations", type = Accessor.Type.GETTER)
    public Collection<SolrIndexOperationRecordModel> getIndexOperations()
    {
        return (Collection<SolrIndexOperationRecordModel>)getPersistenceContext().getPropertyValue("indexOperations");
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "indexTime", type = Accessor.Type.GETTER)
    public Date getIndexTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("indexTime");
    }


    @Accessor(qualifier = "serverMode", type = Accessor.Type.GETTER)
    public SolrServerModes getServerMode()
    {
        return (SolrServerModes)getPersistenceContext().getPropertyValue("serverMode");
    }


    @Accessor(qualifier = "coreName", type = Accessor.Type.SETTER)
    public void setCoreName(String value)
    {
        getPersistenceContext().setPropertyValue("coreName", value);
    }


    @Accessor(qualifier = "currentIndexDataSubDirectory", type = Accessor.Type.SETTER)
    public void setCurrentIndexDataSubDirectory(String value)
    {
        getPersistenceContext().setPropertyValue("currentIndexDataSubDirectory", value);
    }


    @Accessor(qualifier = "indexName", type = Accessor.Type.SETTER)
    public void setIndexName(String value)
    {
        getPersistenceContext().setPropertyValue("indexName", value);
    }


    @Accessor(qualifier = "indexOperations", type = Accessor.Type.SETTER)
    public void setIndexOperations(Collection<SolrIndexOperationRecordModel> value)
    {
        getPersistenceContext().setPropertyValue("indexOperations", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "indexTime", type = Accessor.Type.SETTER)
    public void setIndexTime(Date value)
    {
        getPersistenceContext().setPropertyValue("indexTime", value);
    }


    @Accessor(qualifier = "serverMode", type = Accessor.Type.SETTER)
    public void setServerMode(SolrServerModes value)
    {
        getPersistenceContext().setPropertyValue("serverMode", value);
    }
}
