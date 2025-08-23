package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationStatus;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import java.util.Date;

public class SolrIndexOperationModel extends ItemModel
{
    public static final String _TYPECODE = "SolrIndexOperation";
    public static final String _SOLRINDEX2SOLRINDEXOPERATION = "SolrIndex2SolrIndexOperation";
    public static final String ID = "id";
    public static final String OPERATION = "operation";
    public static final String EXTERNAL = "external";
    public static final String STATUS = "status";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String INDEX = "index";


    public SolrIndexOperationModel()
    {
    }


    public SolrIndexOperationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexOperationModel(long _id, SolrIndexModel _index, IndexerOperationValues _operation, IndexerOperationStatus _status)
    {
        setId(_id);
        setIndex(_index);
        setOperation(_operation);
        setStatus(_status);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexOperationModel(long _id, SolrIndexModel _index, IndexerOperationValues _operation, ItemModel _owner, IndexerOperationStatus _status)
    {
        setId(_id);
        setIndex(_index);
        setOperation(_operation);
        setOwner(_owner);
        setStatus(_status);
    }


    @Accessor(qualifier = "endTime", type = Accessor.Type.GETTER)
    public Date getEndTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("endTime");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public long getId()
    {
        return toPrimitive((Long)getPersistenceContext().getPropertyValue("id"));
    }


    @Accessor(qualifier = "index", type = Accessor.Type.GETTER)
    public SolrIndexModel getIndex()
    {
        return (SolrIndexModel)getPersistenceContext().getPropertyValue("index");
    }


    @Accessor(qualifier = "operation", type = Accessor.Type.GETTER)
    public IndexerOperationValues getOperation()
    {
        return (IndexerOperationValues)getPersistenceContext().getPropertyValue("operation");
    }


    @Accessor(qualifier = "startTime", type = Accessor.Type.GETTER)
    public Date getStartTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("startTime");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public IndexerOperationStatus getStatus()
    {
        return (IndexerOperationStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "external", type = Accessor.Type.GETTER)
    public boolean isExternal()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("external"));
    }


    @Accessor(qualifier = "endTime", type = Accessor.Type.SETTER)
    public void setEndTime(Date value)
    {
        getPersistenceContext().setPropertyValue("endTime", value);
    }


    @Accessor(qualifier = "external", type = Accessor.Type.SETTER)
    public void setExternal(boolean value)
    {
        getPersistenceContext().setPropertyValue("external", toObject(value));
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(long value)
    {
        getPersistenceContext().setPropertyValue("id", toObject(value));
    }


    @Accessor(qualifier = "index", type = Accessor.Type.SETTER)
    public void setIndex(SolrIndexModel value)
    {
        getPersistenceContext().setPropertyValue("index", value);
    }


    @Accessor(qualifier = "operation", type = Accessor.Type.SETTER)
    public void setOperation(IndexerOperationValues value)
    {
        getPersistenceContext().setPropertyValue("operation", value);
    }


    @Accessor(qualifier = "startTime", type = Accessor.Type.SETTER)
    public void setStartTime(Date value)
    {
        getPersistenceContext().setPropertyValue("startTime", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(IndexerOperationStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }
}
