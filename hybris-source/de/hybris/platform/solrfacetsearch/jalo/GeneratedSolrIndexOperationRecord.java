package de.hybris.platform.solrfacetsearch.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.indexer.SolrIndexedCoresRecord;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrIndexOperationRecord extends GenericItem
{
    public static final String STARTTIME = "startTime";
    public static final String FINISHTIME = "finishTime";
    public static final String MODE = "mode";
    public static final String STATUS = "status";
    public static final String THREADID = "threadId";
    public static final String CLUSTERID = "clusterId";
    public static final String FAILEDREASON = "failedReason";
    public static final String SOLRINDEXCORERECORDPOS = "solrIndexCoreRecordPOS";
    public static final String SOLRINDEXCORERECORD = "solrIndexCoreRecord";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrIndexOperationRecord> SOLRINDEXCORERECORDHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXOPERATIONRECORD, false, "solrIndexCoreRecord", "solrIndexCoreRecordPOS", true, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("startTime", Item.AttributeMode.INITIAL);
        tmp.put("finishTime", Item.AttributeMode.INITIAL);
        tmp.put("mode", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("threadId", Item.AttributeMode.INITIAL);
        tmp.put("clusterId", Item.AttributeMode.INITIAL);
        tmp.put("failedReason", Item.AttributeMode.INITIAL);
        tmp.put("solrIndexCoreRecordPOS", Item.AttributeMode.INITIAL);
        tmp.put("solrIndexCoreRecord", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getClusterId(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "clusterId");
    }


    public Integer getClusterId()
    {
        return getClusterId(getSession().getSessionContext());
    }


    public int getClusterIdAsPrimitive(SessionContext ctx)
    {
        Integer value = getClusterId(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getClusterIdAsPrimitive()
    {
        return getClusterIdAsPrimitive(getSession().getSessionContext());
    }


    public void setClusterId(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "clusterId", value);
    }


    public void setClusterId(Integer value)
    {
        setClusterId(getSession().getSessionContext(), value);
    }


    public void setClusterId(SessionContext ctx, int value)
    {
        setClusterId(ctx, Integer.valueOf(value));
    }


    public void setClusterId(int value)
    {
        setClusterId(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOLRINDEXCORERECORDHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getFailedReason(SessionContext ctx)
    {
        return (String)getProperty(ctx, "failedReason");
    }


    public String getFailedReason()
    {
        return getFailedReason(getSession().getSessionContext());
    }


    public void setFailedReason(SessionContext ctx, String value)
    {
        setProperty(ctx, "failedReason", value);
    }


    public void setFailedReason(String value)
    {
        setFailedReason(getSession().getSessionContext(), value);
    }


    public Date getFinishTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "finishTime");
    }


    public Date getFinishTime()
    {
        return getFinishTime(getSession().getSessionContext());
    }


    public void setFinishTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "finishTime", value);
    }


    public void setFinishTime(Date value)
    {
        setFinishTime(getSession().getSessionContext(), value);
    }


    public EnumerationValue getMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "mode");
    }


    public EnumerationValue getMode()
    {
        return getMode(getSession().getSessionContext());
    }


    public void setMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "mode", value);
    }


    public void setMode(EnumerationValue value)
    {
        setMode(getSession().getSessionContext(), value);
    }


    public SolrIndexedCoresRecord getSolrIndexCoreRecord(SessionContext ctx)
    {
        return (SolrIndexedCoresRecord)getProperty(ctx, "solrIndexCoreRecord");
    }


    public SolrIndexedCoresRecord getSolrIndexCoreRecord()
    {
        return getSolrIndexCoreRecord(getSession().getSessionContext());
    }


    public void setSolrIndexCoreRecord(SessionContext ctx, SolrIndexedCoresRecord value)
    {
        SOLRINDEXCORERECORDHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSolrIndexCoreRecord(SolrIndexedCoresRecord value)
    {
        setSolrIndexCoreRecord(getSession().getSessionContext(), value);
    }


    Integer getSolrIndexCoreRecordPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "solrIndexCoreRecordPOS");
    }


    Integer getSolrIndexCoreRecordPOS()
    {
        return getSolrIndexCoreRecordPOS(getSession().getSessionContext());
    }


    int getSolrIndexCoreRecordPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSolrIndexCoreRecordPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSolrIndexCoreRecordPOSAsPrimitive()
    {
        return getSolrIndexCoreRecordPOSAsPrimitive(getSession().getSessionContext());
    }


    void setSolrIndexCoreRecordPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "solrIndexCoreRecordPOS", value);
    }


    void setSolrIndexCoreRecordPOS(Integer value)
    {
        setSolrIndexCoreRecordPOS(getSession().getSessionContext(), value);
    }


    void setSolrIndexCoreRecordPOS(SessionContext ctx, int value)
    {
        setSolrIndexCoreRecordPOS(ctx, Integer.valueOf(value));
    }


    void setSolrIndexCoreRecordPOS(int value)
    {
        setSolrIndexCoreRecordPOS(getSession().getSessionContext(), value);
    }


    public Date getStartTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "startTime");
    }


    public Date getStartTime()
    {
        return getStartTime(getSession().getSessionContext());
    }


    public void setStartTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "startTime", value);
    }


    public void setStartTime(Date value)
    {
        setStartTime(getSession().getSessionContext(), value);
    }


    public EnumerationValue getStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "status");
    }


    public EnumerationValue getStatus()
    {
        return getStatus(getSession().getSessionContext());
    }


    public void setStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "status", value);
    }


    public void setStatus(EnumerationValue value)
    {
        setStatus(getSession().getSessionContext(), value);
    }


    public String getThreadId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "threadId");
    }


    public String getThreadId()
    {
        return getThreadId(getSession().getSessionContext());
    }


    public void setThreadId(SessionContext ctx, String value)
    {
        setProperty(ctx, "threadId", value);
    }


    public void setThreadId(String value)
    {
        setThreadId(getSession().getSessionContext(), value);
    }
}
