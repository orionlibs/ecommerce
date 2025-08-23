package de.hybris.platform.solrfacetsearch.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationStatus;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.model.indexer.SolrIndexedCoresRecordModel;
import java.util.Date;

public class SolrIndexOperationRecordModel extends ItemModel
{
    public static final String _TYPECODE = "SolrIndexOperationRecord";
    public static final String _INDEXCORE2INDEXOPERATIONRECORDS = "IndexCore2IndexOperationRecords";
    public static final String STARTTIME = "startTime";
    public static final String FINISHTIME = "finishTime";
    public static final String MODE = "mode";
    public static final String STATUS = "status";
    public static final String THREADID = "threadId";
    public static final String CLUSTERID = "clusterId";
    public static final String FAILEDREASON = "failedReason";
    public static final String SOLRINDEXCORERECORDPOS = "solrIndexCoreRecordPOS";
    public static final String SOLRINDEXCORERECORD = "solrIndexCoreRecord";


    public SolrIndexOperationRecordModel()
    {
    }


    public SolrIndexOperationRecordModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexOperationRecordModel(int _clusterId, IndexerOperationValues _mode, SolrIndexedCoresRecordModel _solrIndexCoreRecord, IndexerOperationStatus _status, String _threadId)
    {
        setClusterId(_clusterId);
        setMode(_mode);
        setSolrIndexCoreRecord(_solrIndexCoreRecord);
        setStatus(_status);
        setThreadId(_threadId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrIndexOperationRecordModel(int _clusterId, IndexerOperationValues _mode, ItemModel _owner, SolrIndexedCoresRecordModel _solrIndexCoreRecord, IndexerOperationStatus _status, String _threadId)
    {
        setClusterId(_clusterId);
        setMode(_mode);
        setOwner(_owner);
        setSolrIndexCoreRecord(_solrIndexCoreRecord);
        setStatus(_status);
        setThreadId(_threadId);
    }


    @Accessor(qualifier = "clusterId", type = Accessor.Type.GETTER)
    public int getClusterId()
    {
        return toPrimitive((Integer)getPersistenceContext().getPropertyValue("clusterId"));
    }


    @Accessor(qualifier = "failedReason", type = Accessor.Type.GETTER)
    public String getFailedReason()
    {
        return (String)getPersistenceContext().getPropertyValue("failedReason");
    }


    @Accessor(qualifier = "finishTime", type = Accessor.Type.GETTER)
    public Date getFinishTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("finishTime");
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.GETTER)
    public IndexerOperationValues getMode()
    {
        return (IndexerOperationValues)getPersistenceContext().getPropertyValue("mode");
    }


    @Accessor(qualifier = "solrIndexCoreRecord", type = Accessor.Type.GETTER)
    public SolrIndexedCoresRecordModel getSolrIndexCoreRecord()
    {
        return (SolrIndexedCoresRecordModel)getPersistenceContext().getPropertyValue("solrIndexCoreRecord");
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


    @Accessor(qualifier = "threadId", type = Accessor.Type.GETTER)
    public String getThreadId()
    {
        return (String)getPersistenceContext().getPropertyValue("threadId");
    }


    @Accessor(qualifier = "clusterId", type = Accessor.Type.SETTER)
    public void setClusterId(int value)
    {
        getPersistenceContext().setPropertyValue("clusterId", toObject(value));
    }


    @Accessor(qualifier = "failedReason", type = Accessor.Type.SETTER)
    public void setFailedReason(String value)
    {
        getPersistenceContext().setPropertyValue("failedReason", value);
    }


    @Accessor(qualifier = "finishTime", type = Accessor.Type.SETTER)
    public void setFinishTime(Date value)
    {
        getPersistenceContext().setPropertyValue("finishTime", value);
    }


    @Accessor(qualifier = "mode", type = Accessor.Type.SETTER)
    public void setMode(IndexerOperationValues value)
    {
        getPersistenceContext().setPropertyValue("mode", value);
    }


    @Accessor(qualifier = "solrIndexCoreRecord", type = Accessor.Type.SETTER)
    public void setSolrIndexCoreRecord(SolrIndexedCoresRecordModel value)
    {
        getPersistenceContext().setPropertyValue("solrIndexCoreRecord", value);
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


    @Accessor(qualifier = "threadId", type = Accessor.Type.SETTER)
    public void setThreadId(String value)
    {
        getPersistenceContext().setPropertyValue("threadId", value);
    }
}
