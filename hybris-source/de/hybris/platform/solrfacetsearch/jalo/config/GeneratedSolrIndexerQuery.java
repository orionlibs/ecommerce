package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedSolrIndexerQuery extends GenericItem
{
    public static final String IDENTIFIER = "identifier";
    public static final String TYPE = "type";
    public static final String QUERY = "query";
    public static final String INJECTLASTINDEXTIME = "injectLastIndexTime";
    public static final String INJECTCURRENTTIME = "injectCurrentTime";
    public static final String INJECTCURRENTDATE = "injectCurrentDate";
    public static final String USER = "user";
    public static final String PARAMETERPROVIDER = "parameterProvider";
    public static final String SOLRINDEXEDTYPEPOS = "solrIndexedTypePOS";
    public static final String SOLRINDEXEDTYPE = "solrIndexedType";
    public static final String SOLRINDEXERQUERYPARAMETERS = "solrIndexerQueryParameters";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrIndexerQuery> SOLRINDEXEDTYPEHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERQUERY, false, "solrIndexedType", "solrIndexedTypePOS", true, true, 2);
    protected static final OneToManyHandler<SolrIndexerQueryParameter> SOLRINDEXERQUERYPARAMETERSHANDLER = new OneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERQUERYPARAMETER, true, "solrIndexerQuery", "solrIndexerQueryPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("identifier", Item.AttributeMode.INITIAL);
        tmp.put("type", Item.AttributeMode.INITIAL);
        tmp.put("query", Item.AttributeMode.INITIAL);
        tmp.put("injectLastIndexTime", Item.AttributeMode.INITIAL);
        tmp.put("injectCurrentTime", Item.AttributeMode.INITIAL);
        tmp.put("injectCurrentDate", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        tmp.put("parameterProvider", Item.AttributeMode.INITIAL);
        tmp.put("solrIndexedTypePOS", Item.AttributeMode.INITIAL);
        tmp.put("solrIndexedType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOLRINDEXEDTYPEHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getIdentifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "identifier");
    }


    public String getIdentifier()
    {
        return getIdentifier(getSession().getSessionContext());
    }


    protected void setIdentifier(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'identifier' is not changeable", 0);
        }
        setProperty(ctx, "identifier", value);
    }


    protected void setIdentifier(String value)
    {
        setIdentifier(getSession().getSessionContext(), value);
    }


    public Boolean isInjectCurrentDate(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "injectCurrentDate");
    }


    public Boolean isInjectCurrentDate()
    {
        return isInjectCurrentDate(getSession().getSessionContext());
    }


    public boolean isInjectCurrentDateAsPrimitive(SessionContext ctx)
    {
        Boolean value = isInjectCurrentDate(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isInjectCurrentDateAsPrimitive()
    {
        return isInjectCurrentDateAsPrimitive(getSession().getSessionContext());
    }


    public void setInjectCurrentDate(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "injectCurrentDate", value);
    }


    public void setInjectCurrentDate(Boolean value)
    {
        setInjectCurrentDate(getSession().getSessionContext(), value);
    }


    public void setInjectCurrentDate(SessionContext ctx, boolean value)
    {
        setInjectCurrentDate(ctx, Boolean.valueOf(value));
    }


    public void setInjectCurrentDate(boolean value)
    {
        setInjectCurrentDate(getSession().getSessionContext(), value);
    }


    public Boolean isInjectCurrentTime(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "injectCurrentTime");
    }


    public Boolean isInjectCurrentTime()
    {
        return isInjectCurrentTime(getSession().getSessionContext());
    }


    public boolean isInjectCurrentTimeAsPrimitive(SessionContext ctx)
    {
        Boolean value = isInjectCurrentTime(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isInjectCurrentTimeAsPrimitive()
    {
        return isInjectCurrentTimeAsPrimitive(getSession().getSessionContext());
    }


    public void setInjectCurrentTime(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "injectCurrentTime", value);
    }


    public void setInjectCurrentTime(Boolean value)
    {
        setInjectCurrentTime(getSession().getSessionContext(), value);
    }


    public void setInjectCurrentTime(SessionContext ctx, boolean value)
    {
        setInjectCurrentTime(ctx, Boolean.valueOf(value));
    }


    public void setInjectCurrentTime(boolean value)
    {
        setInjectCurrentTime(getSession().getSessionContext(), value);
    }


    public Boolean isInjectLastIndexTime(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "injectLastIndexTime");
    }


    public Boolean isInjectLastIndexTime()
    {
        return isInjectLastIndexTime(getSession().getSessionContext());
    }


    public boolean isInjectLastIndexTimeAsPrimitive(SessionContext ctx)
    {
        Boolean value = isInjectLastIndexTime(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isInjectLastIndexTimeAsPrimitive()
    {
        return isInjectLastIndexTimeAsPrimitive(getSession().getSessionContext());
    }


    public void setInjectLastIndexTime(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "injectLastIndexTime", value);
    }


    public void setInjectLastIndexTime(Boolean value)
    {
        setInjectLastIndexTime(getSession().getSessionContext(), value);
    }


    public void setInjectLastIndexTime(SessionContext ctx, boolean value)
    {
        setInjectLastIndexTime(ctx, Boolean.valueOf(value));
    }


    public void setInjectLastIndexTime(boolean value)
    {
        setInjectLastIndexTime(getSession().getSessionContext(), value);
    }


    public String getParameterProvider(SessionContext ctx)
    {
        return (String)getProperty(ctx, "parameterProvider");
    }


    public String getParameterProvider()
    {
        return getParameterProvider(getSession().getSessionContext());
    }


    public void setParameterProvider(SessionContext ctx, String value)
    {
        setProperty(ctx, "parameterProvider", value);
    }


    public void setParameterProvider(String value)
    {
        setParameterProvider(getSession().getSessionContext(), value);
    }


    public String getQuery(SessionContext ctx)
    {
        return (String)getProperty(ctx, "query");
    }


    public String getQuery()
    {
        return getQuery(getSession().getSessionContext());
    }


    public void setQuery(SessionContext ctx, String value)
    {
        setProperty(ctx, "query", value);
    }


    public void setQuery(String value)
    {
        setQuery(getSession().getSessionContext(), value);
    }


    public SolrIndexedType getSolrIndexedType(SessionContext ctx)
    {
        return (SolrIndexedType)getProperty(ctx, "solrIndexedType");
    }


    public SolrIndexedType getSolrIndexedType()
    {
        return getSolrIndexedType(getSession().getSessionContext());
    }


    public void setSolrIndexedType(SessionContext ctx, SolrIndexedType value)
    {
        SOLRINDEXEDTYPEHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSolrIndexedType(SolrIndexedType value)
    {
        setSolrIndexedType(getSession().getSessionContext(), value);
    }


    Integer getSolrIndexedTypePOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "solrIndexedTypePOS");
    }


    Integer getSolrIndexedTypePOS()
    {
        return getSolrIndexedTypePOS(getSession().getSessionContext());
    }


    int getSolrIndexedTypePOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSolrIndexedTypePOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSolrIndexedTypePOSAsPrimitive()
    {
        return getSolrIndexedTypePOSAsPrimitive(getSession().getSessionContext());
    }


    void setSolrIndexedTypePOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "solrIndexedTypePOS", value);
    }


    void setSolrIndexedTypePOS(Integer value)
    {
        setSolrIndexedTypePOS(getSession().getSessionContext(), value);
    }


    void setSolrIndexedTypePOS(SessionContext ctx, int value)
    {
        setSolrIndexedTypePOS(ctx, Integer.valueOf(value));
    }


    void setSolrIndexedTypePOS(int value)
    {
        setSolrIndexedTypePOS(getSession().getSessionContext(), value);
    }


    public List<SolrIndexerQueryParameter> getSolrIndexerQueryParameters(SessionContext ctx)
    {
        return (List<SolrIndexerQueryParameter>)SOLRINDEXERQUERYPARAMETERSHANDLER.getValues(ctx, (Item)this);
    }


    public List<SolrIndexerQueryParameter> getSolrIndexerQueryParameters()
    {
        return getSolrIndexerQueryParameters(getSession().getSessionContext());
    }


    public void setSolrIndexerQueryParameters(SessionContext ctx, List<SolrIndexerQueryParameter> value)
    {
        SOLRINDEXERQUERYPARAMETERSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setSolrIndexerQueryParameters(List<SolrIndexerQueryParameter> value)
    {
        setSolrIndexerQueryParameters(getSession().getSessionContext(), value);
    }


    public void addToSolrIndexerQueryParameters(SessionContext ctx, SolrIndexerQueryParameter value)
    {
        SOLRINDEXERQUERYPARAMETERSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToSolrIndexerQueryParameters(SolrIndexerQueryParameter value)
    {
        addToSolrIndexerQueryParameters(getSession().getSessionContext(), value);
    }


    public void removeFromSolrIndexerQueryParameters(SessionContext ctx, SolrIndexerQueryParameter value)
    {
        SOLRINDEXERQUERYPARAMETERSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromSolrIndexerQueryParameters(SolrIndexerQueryParameter value)
    {
        removeFromSolrIndexerQueryParameters(getSession().getSessionContext(), value);
    }


    public EnumerationValue getType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "type");
    }


    public EnumerationValue getType()
    {
        return getType(getSession().getSessionContext());
    }


    protected void setType(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'type' is not changeable", 0);
        }
        setProperty(ctx, "type", value);
    }


    protected void setType(EnumerationValue value)
    {
        setType(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        setProperty(ctx, "user", value);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }
}
