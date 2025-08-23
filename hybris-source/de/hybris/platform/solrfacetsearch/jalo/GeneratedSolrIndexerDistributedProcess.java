package de.hybris.platform.solrfacetsearch.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.processing.jalo.SimpleDistributedProcess;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrIndexerDistributedProcess extends SimpleDistributedProcess
{
    public static final String SESSIONUSER = "sessionUser";
    public static final String SESSIONLANGUAGE = "sessionLanguage";
    public static final String SESSIONCURRENCY = "sessionCurrency";
    public static final String INDEXOPERATIONID = "indexOperationId";
    public static final String INDEXOPERATION = "indexOperation";
    public static final String EXTERNALINDEXOPERATION = "externalIndexOperation";
    public static final String FACETSEARCHCONFIG = "facetSearchConfig";
    public static final String INDEXEDTYPE = "indexedType";
    public static final String INDEXEDPROPERTIES = "indexedProperties";
    public static final String INDEX = "index";
    public static final String INDEXERHINTS = "indexerHints";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleDistributedProcess.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("sessionUser", Item.AttributeMode.INITIAL);
        tmp.put("sessionLanguage", Item.AttributeMode.INITIAL);
        tmp.put("sessionCurrency", Item.AttributeMode.INITIAL);
        tmp.put("indexOperationId", Item.AttributeMode.INITIAL);
        tmp.put("indexOperation", Item.AttributeMode.INITIAL);
        tmp.put("externalIndexOperation", Item.AttributeMode.INITIAL);
        tmp.put("facetSearchConfig", Item.AttributeMode.INITIAL);
        tmp.put("indexedType", Item.AttributeMode.INITIAL);
        tmp.put("indexedProperties", Item.AttributeMode.INITIAL);
        tmp.put("index", Item.AttributeMode.INITIAL);
        tmp.put("indexerHints", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isExternalIndexOperation(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "externalIndexOperation");
    }


    public Boolean isExternalIndexOperation()
    {
        return isExternalIndexOperation(getSession().getSessionContext());
    }


    public boolean isExternalIndexOperationAsPrimitive(SessionContext ctx)
    {
        Boolean value = isExternalIndexOperation(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isExternalIndexOperationAsPrimitive()
    {
        return isExternalIndexOperationAsPrimitive(getSession().getSessionContext());
    }


    public void setExternalIndexOperation(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "externalIndexOperation", value);
    }


    public void setExternalIndexOperation(Boolean value)
    {
        setExternalIndexOperation(getSession().getSessionContext(), value);
    }


    public void setExternalIndexOperation(SessionContext ctx, boolean value)
    {
        setExternalIndexOperation(ctx, Boolean.valueOf(value));
    }


    public void setExternalIndexOperation(boolean value)
    {
        setExternalIndexOperation(getSession().getSessionContext(), value);
    }


    public String getFacetSearchConfig(SessionContext ctx)
    {
        return (String)getProperty(ctx, "facetSearchConfig");
    }


    public String getFacetSearchConfig()
    {
        return getFacetSearchConfig(getSession().getSessionContext());
    }


    public void setFacetSearchConfig(SessionContext ctx, String value)
    {
        setProperty(ctx, "facetSearchConfig", value);
    }


    public void setFacetSearchConfig(String value)
    {
        setFacetSearchConfig(getSession().getSessionContext(), value);
    }


    public String getIndex(SessionContext ctx)
    {
        return (String)getProperty(ctx, "index");
    }


    public String getIndex()
    {
        return getIndex(getSession().getSessionContext());
    }


    public void setIndex(SessionContext ctx, String value)
    {
        setProperty(ctx, "index", value);
    }


    public void setIndex(String value)
    {
        setIndex(getSession().getSessionContext(), value);
    }


    public Collection<String> getIndexedProperties(SessionContext ctx)
    {
        Collection<String> coll = (Collection<String>)getProperty(ctx, "indexedProperties");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<String> getIndexedProperties()
    {
        return getIndexedProperties(getSession().getSessionContext());
    }


    public void setIndexedProperties(SessionContext ctx, Collection<String> value)
    {
        setProperty(ctx, "indexedProperties", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setIndexedProperties(Collection<String> value)
    {
        setIndexedProperties(getSession().getSessionContext(), value);
    }


    public String getIndexedType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "indexedType");
    }


    public String getIndexedType()
    {
        return getIndexedType(getSession().getSessionContext());
    }


    public void setIndexedType(SessionContext ctx, String value)
    {
        setProperty(ctx, "indexedType", value);
    }


    public void setIndexedType(String value)
    {
        setIndexedType(getSession().getSessionContext(), value);
    }


    public Map<String, String> getAllIndexerHints(SessionContext ctx)
    {
        Map<String, String> map = (Map<String, String>)getProperty(ctx, "indexerHints");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllIndexerHints()
    {
        return getAllIndexerHints(getSession().getSessionContext());
    }


    public void setAllIndexerHints(SessionContext ctx, Map<String, String> value)
    {
        setProperty(ctx, "indexerHints", value);
    }


    public void setAllIndexerHints(Map<String, String> value)
    {
        setAllIndexerHints(getSession().getSessionContext(), value);
    }


    public EnumerationValue getIndexOperation(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "indexOperation");
    }


    public EnumerationValue getIndexOperation()
    {
        return getIndexOperation(getSession().getSessionContext());
    }


    public void setIndexOperation(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "indexOperation", value);
    }


    public void setIndexOperation(EnumerationValue value)
    {
        setIndexOperation(getSession().getSessionContext(), value);
    }


    public Long getIndexOperationId(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "indexOperationId");
    }


    public Long getIndexOperationId()
    {
        return getIndexOperationId(getSession().getSessionContext());
    }


    public long getIndexOperationIdAsPrimitive(SessionContext ctx)
    {
        Long value = getIndexOperationId(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getIndexOperationIdAsPrimitive()
    {
        return getIndexOperationIdAsPrimitive(getSession().getSessionContext());
    }


    public void setIndexOperationId(SessionContext ctx, Long value)
    {
        setProperty(ctx, "indexOperationId", value);
    }


    public void setIndexOperationId(Long value)
    {
        setIndexOperationId(getSession().getSessionContext(), value);
    }


    public void setIndexOperationId(SessionContext ctx, long value)
    {
        setIndexOperationId(ctx, Long.valueOf(value));
    }


    public void setIndexOperationId(long value)
    {
        setIndexOperationId(getSession().getSessionContext(), value);
    }


    public String getSessionCurrency(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sessionCurrency");
    }


    public String getSessionCurrency()
    {
        return getSessionCurrency(getSession().getSessionContext());
    }


    public void setSessionCurrency(SessionContext ctx, String value)
    {
        setProperty(ctx, "sessionCurrency", value);
    }


    public void setSessionCurrency(String value)
    {
        setSessionCurrency(getSession().getSessionContext(), value);
    }


    public String getSessionLanguage(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sessionLanguage");
    }


    public String getSessionLanguage()
    {
        return getSessionLanguage(getSession().getSessionContext());
    }


    public void setSessionLanguage(SessionContext ctx, String value)
    {
        setProperty(ctx, "sessionLanguage", value);
    }


    public void setSessionLanguage(String value)
    {
        setSessionLanguage(getSession().getSessionContext(), value);
    }


    public String getSessionUser(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sessionUser");
    }


    public String getSessionUser()
    {
        return getSessionUser(getSession().getSessionContext());
    }


    public void setSessionUser(SessionContext ctx, String value)
    {
        setProperty(ctx, "sessionUser", value);
    }


    public void setSessionUser(String value)
    {
        setSessionUser(getSession().getSessionContext(), value);
    }
}
