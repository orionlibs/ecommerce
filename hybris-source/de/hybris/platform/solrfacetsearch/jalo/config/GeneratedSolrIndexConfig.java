package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrIndexConfig extends GenericItem
{
    public static final String NAME = "name";
    public static final String BATCHSIZE = "batchSize";
    public static final String EXPORTPATH = "exportPath";
    public static final String NUMBEROFTHREADS = "numberOfThreads";
    public static final String INDEXMODE = "indexMode";
    public static final String COMMITMODE = "commitMode";
    public static final String OPTIMIZEMODE = "optimizeMode";
    public static final String IGNOREERRORS = "ignoreErrors";
    public static final String LEGACYMODE = "legacyMode";
    public static final String MAXRETRIES = "maxRetries";
    public static final String MAXBATCHRETRIES = "maxBatchRetries";
    public static final String DISTRIBUTEDINDEXING = "distributedIndexing";
    public static final String NODEGROUP = "nodeGroup";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("batchSize", Item.AttributeMode.INITIAL);
        tmp.put("exportPath", Item.AttributeMode.INITIAL);
        tmp.put("numberOfThreads", Item.AttributeMode.INITIAL);
        tmp.put("indexMode", Item.AttributeMode.INITIAL);
        tmp.put("commitMode", Item.AttributeMode.INITIAL);
        tmp.put("optimizeMode", Item.AttributeMode.INITIAL);
        tmp.put("ignoreErrors", Item.AttributeMode.INITIAL);
        tmp.put("legacyMode", Item.AttributeMode.INITIAL);
        tmp.put("maxRetries", Item.AttributeMode.INITIAL);
        tmp.put("maxBatchRetries", Item.AttributeMode.INITIAL);
        tmp.put("distributedIndexing", Item.AttributeMode.INITIAL);
        tmp.put("nodeGroup", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getBatchSize(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "batchSize");
    }


    public Integer getBatchSize()
    {
        return getBatchSize(getSession().getSessionContext());
    }


    public int getBatchSizeAsPrimitive(SessionContext ctx)
    {
        Integer value = getBatchSize(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getBatchSizeAsPrimitive()
    {
        return getBatchSizeAsPrimitive(getSession().getSessionContext());
    }


    public void setBatchSize(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "batchSize", value);
    }


    public void setBatchSize(Integer value)
    {
        setBatchSize(getSession().getSessionContext(), value);
    }


    public void setBatchSize(SessionContext ctx, int value)
    {
        setBatchSize(ctx, Integer.valueOf(value));
    }


    public void setBatchSize(int value)
    {
        setBatchSize(getSession().getSessionContext(), value);
    }


    public EnumerationValue getCommitMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "commitMode");
    }


    public EnumerationValue getCommitMode()
    {
        return getCommitMode(getSession().getSessionContext());
    }


    public void setCommitMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "commitMode", value);
    }


    public void setCommitMode(EnumerationValue value)
    {
        setCommitMode(getSession().getSessionContext(), value);
    }


    public Boolean isDistributedIndexing(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "distributedIndexing");
    }


    public Boolean isDistributedIndexing()
    {
        return isDistributedIndexing(getSession().getSessionContext());
    }


    public boolean isDistributedIndexingAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDistributedIndexing(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDistributedIndexingAsPrimitive()
    {
        return isDistributedIndexingAsPrimitive(getSession().getSessionContext());
    }


    public void setDistributedIndexing(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "distributedIndexing", value);
    }


    public void setDistributedIndexing(Boolean value)
    {
        setDistributedIndexing(getSession().getSessionContext(), value);
    }


    public void setDistributedIndexing(SessionContext ctx, boolean value)
    {
        setDistributedIndexing(ctx, Boolean.valueOf(value));
    }


    public void setDistributedIndexing(boolean value)
    {
        setDistributedIndexing(getSession().getSessionContext(), value);
    }


    public String getExportPath(SessionContext ctx)
    {
        return (String)getProperty(ctx, "exportPath");
    }


    public String getExportPath()
    {
        return getExportPath(getSession().getSessionContext());
    }


    public void setExportPath(SessionContext ctx, String value)
    {
        setProperty(ctx, "exportPath", value);
    }


    public void setExportPath(String value)
    {
        setExportPath(getSession().getSessionContext(), value);
    }


    public Boolean isIgnoreErrors(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "ignoreErrors");
    }


    public Boolean isIgnoreErrors()
    {
        return isIgnoreErrors(getSession().getSessionContext());
    }


    public boolean isIgnoreErrorsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIgnoreErrors(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIgnoreErrorsAsPrimitive()
    {
        return isIgnoreErrorsAsPrimitive(getSession().getSessionContext());
    }


    public void setIgnoreErrors(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "ignoreErrors", value);
    }


    public void setIgnoreErrors(Boolean value)
    {
        setIgnoreErrors(getSession().getSessionContext(), value);
    }


    public void setIgnoreErrors(SessionContext ctx, boolean value)
    {
        setIgnoreErrors(ctx, Boolean.valueOf(value));
    }


    public void setIgnoreErrors(boolean value)
    {
        setIgnoreErrors(getSession().getSessionContext(), value);
    }


    public EnumerationValue getIndexMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "indexMode");
    }


    public EnumerationValue getIndexMode()
    {
        return getIndexMode(getSession().getSessionContext());
    }


    public void setIndexMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "indexMode", value);
    }


    public void setIndexMode(EnumerationValue value)
    {
        setIndexMode(getSession().getSessionContext(), value);
    }


    public Boolean isLegacyMode(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "legacyMode");
    }


    public Boolean isLegacyMode()
    {
        return isLegacyMode(getSession().getSessionContext());
    }


    public boolean isLegacyModeAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLegacyMode(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLegacyModeAsPrimitive()
    {
        return isLegacyModeAsPrimitive(getSession().getSessionContext());
    }


    public void setLegacyMode(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "legacyMode", value);
    }


    public void setLegacyMode(Boolean value)
    {
        setLegacyMode(getSession().getSessionContext(), value);
    }


    public void setLegacyMode(SessionContext ctx, boolean value)
    {
        setLegacyMode(ctx, Boolean.valueOf(value));
    }


    public void setLegacyMode(boolean value)
    {
        setLegacyMode(getSession().getSessionContext(), value);
    }


    public Integer getMaxBatchRetries(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxBatchRetries");
    }


    public Integer getMaxBatchRetries()
    {
        return getMaxBatchRetries(getSession().getSessionContext());
    }


    public int getMaxBatchRetriesAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxBatchRetries(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxBatchRetriesAsPrimitive()
    {
        return getMaxBatchRetriesAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxBatchRetries(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxBatchRetries", value);
    }


    public void setMaxBatchRetries(Integer value)
    {
        setMaxBatchRetries(getSession().getSessionContext(), value);
    }


    public void setMaxBatchRetries(SessionContext ctx, int value)
    {
        setMaxBatchRetries(ctx, Integer.valueOf(value));
    }


    public void setMaxBatchRetries(int value)
    {
        setMaxBatchRetries(getSession().getSessionContext(), value);
    }


    public Integer getMaxRetries(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxRetries");
    }


    public Integer getMaxRetries()
    {
        return getMaxRetries(getSession().getSessionContext());
    }


    public int getMaxRetriesAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxRetries(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxRetriesAsPrimitive()
    {
        return getMaxRetriesAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxRetries(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxRetries", value);
    }


    public void setMaxRetries(Integer value)
    {
        setMaxRetries(getSession().getSessionContext(), value);
    }


    public void setMaxRetries(SessionContext ctx, int value)
    {
        setMaxRetries(ctx, Integer.valueOf(value));
    }


    public void setMaxRetries(int value)
    {
        setMaxRetries(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    protected void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'name' is not changeable", 0);
        }
        setProperty(ctx, "name", value);
    }


    protected void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public String getNodeGroup(SessionContext ctx)
    {
        return (String)getProperty(ctx, "nodeGroup");
    }


    public String getNodeGroup()
    {
        return getNodeGroup(getSession().getSessionContext());
    }


    public void setNodeGroup(SessionContext ctx, String value)
    {
        setProperty(ctx, "nodeGroup", value);
    }


    public void setNodeGroup(String value)
    {
        setNodeGroup(getSession().getSessionContext(), value);
    }


    public Integer getNumberOfThreads(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "numberOfThreads");
    }


    public Integer getNumberOfThreads()
    {
        return getNumberOfThreads(getSession().getSessionContext());
    }


    public int getNumberOfThreadsAsPrimitive(SessionContext ctx)
    {
        Integer value = getNumberOfThreads(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getNumberOfThreadsAsPrimitive()
    {
        return getNumberOfThreadsAsPrimitive(getSession().getSessionContext());
    }


    public void setNumberOfThreads(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "numberOfThreads", value);
    }


    public void setNumberOfThreads(Integer value)
    {
        setNumberOfThreads(getSession().getSessionContext(), value);
    }


    public void setNumberOfThreads(SessionContext ctx, int value)
    {
        setNumberOfThreads(ctx, Integer.valueOf(value));
    }


    public void setNumberOfThreads(int value)
    {
        setNumberOfThreads(getSession().getSessionContext(), value);
    }


    public EnumerationValue getOptimizeMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "optimizeMode");
    }


    public EnumerationValue getOptimizeMode()
    {
        return getOptimizeMode(getSession().getSessionContext());
    }


    public void setOptimizeMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "optimizeMode", value);
    }


    public void setOptimizeMode(EnumerationValue value)
    {
        setOptimizeMode(getSession().getSessionContext(), value);
    }
}
