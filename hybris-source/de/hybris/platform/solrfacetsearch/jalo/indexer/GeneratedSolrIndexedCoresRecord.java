package de.hybris.platform.solrfacetsearch.jalo.indexer;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.solrfacetsearch.jalo.SolrIndexOperationRecord;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrIndexedCoresRecord extends GenericItem
{
    public static final String CORENAME = "coreName";
    public static final String INDEXNAME = "indexName";
    public static final String INDEXTIME = "indexTime";
    public static final String CURRENTINDEXDATASUBDIRECTORY = "currentIndexDataSubDirectory";
    public static final String SERVERMODE = "serverMode";
    public static final String INDEXOPERATIONS = "indexOperations";
    protected static final OneToManyHandler<SolrIndexOperationRecord> INDEXOPERATIONSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXOPERATIONRECORD, true, "solrIndexCoreRecord", "solrIndexCoreRecordPOS", true, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("coreName", Item.AttributeMode.INITIAL);
        tmp.put("indexName", Item.AttributeMode.INITIAL);
        tmp.put("indexTime", Item.AttributeMode.INITIAL);
        tmp.put("currentIndexDataSubDirectory", Item.AttributeMode.INITIAL);
        tmp.put("serverMode", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCoreName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "coreName");
    }


    public String getCoreName()
    {
        return getCoreName(getSession().getSessionContext());
    }


    protected void setCoreName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'coreName' is not changeable", 0);
        }
        setProperty(ctx, "coreName", value);
    }


    protected void setCoreName(String value)
    {
        setCoreName(getSession().getSessionContext(), value);
    }


    public String getCurrentIndexDataSubDirectory(SessionContext ctx)
    {
        return (String)getProperty(ctx, "currentIndexDataSubDirectory");
    }


    public String getCurrentIndexDataSubDirectory()
    {
        return getCurrentIndexDataSubDirectory(getSession().getSessionContext());
    }


    public void setCurrentIndexDataSubDirectory(SessionContext ctx, String value)
    {
        setProperty(ctx, "currentIndexDataSubDirectory", value);
    }


    public void setCurrentIndexDataSubDirectory(String value)
    {
        setCurrentIndexDataSubDirectory(getSession().getSessionContext(), value);
    }


    public String getIndexName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexName");
    }


    public String getIndexName()
    {
        return getIndexName(getSession().getSessionContext());
    }


    protected void setIndexName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'indexName' is not changeable", 0);
        }
        setProperty(ctx, "indexName", value);
    }


    protected void setIndexName(String value)
    {
        setIndexName(getSession().getSessionContext(), value);
    }


    public Collection<SolrIndexOperationRecord> getIndexOperations(SessionContext ctx)
    {
        return INDEXOPERATIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<SolrIndexOperationRecord> getIndexOperations()
    {
        return getIndexOperations(getSession().getSessionContext());
    }


    public void setIndexOperations(SessionContext ctx, Collection<SolrIndexOperationRecord> value)
    {
        INDEXOPERATIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setIndexOperations(Collection<SolrIndexOperationRecord> value)
    {
        setIndexOperations(getSession().getSessionContext(), value);
    }


    public void addToIndexOperations(SessionContext ctx, SolrIndexOperationRecord value)
    {
        INDEXOPERATIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToIndexOperations(SolrIndexOperationRecord value)
    {
        addToIndexOperations(getSession().getSessionContext(), value);
    }


    public void removeFromIndexOperations(SessionContext ctx, SolrIndexOperationRecord value)
    {
        INDEXOPERATIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromIndexOperations(SolrIndexOperationRecord value)
    {
        removeFromIndexOperations(getSession().getSessionContext(), value);
    }


    public Date getIndexTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "indexTime");
    }


    public Date getIndexTime()
    {
        return getIndexTime(getSession().getSessionContext());
    }


    public void setIndexTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "indexTime", value);
    }


    public void setIndexTime(Date value)
    {
        setIndexTime(getSession().getSessionContext(), value);
    }


    public EnumerationValue getServerMode(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "serverMode");
    }


    public EnumerationValue getServerMode()
    {
        return getServerMode(getSession().getSessionContext());
    }


    public void setServerMode(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "serverMode", value);
    }


    public void setServerMode(EnumerationValue value)
    {
        setServerMode(getSession().getSessionContext(), value);
    }
}
