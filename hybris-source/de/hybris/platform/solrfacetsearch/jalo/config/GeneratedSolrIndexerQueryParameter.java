package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrIndexerQueryParameter extends GenericItem
{
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String SOLRINDEXERQUERYPOS = "solrIndexerQueryPOS";
    public static final String SOLRINDEXERQUERY = "solrIndexerQuery";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrIndexerQueryParameter> SOLRINDEXERQUERYHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXERQUERYPARAMETER, false, "solrIndexerQuery", "solrIndexerQueryPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("value", Item.AttributeMode.INITIAL);
        tmp.put("solrIndexerQueryPOS", Item.AttributeMode.INITIAL);
        tmp.put("solrIndexerQuery", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOLRINDEXERQUERYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public SolrIndexerQuery getSolrIndexerQuery(SessionContext ctx)
    {
        return (SolrIndexerQuery)getProperty(ctx, "solrIndexerQuery");
    }


    public SolrIndexerQuery getSolrIndexerQuery()
    {
        return getSolrIndexerQuery(getSession().getSessionContext());
    }


    public void setSolrIndexerQuery(SessionContext ctx, SolrIndexerQuery value)
    {
        SOLRINDEXERQUERYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSolrIndexerQuery(SolrIndexerQuery value)
    {
        setSolrIndexerQuery(getSession().getSessionContext(), value);
    }


    Integer getSolrIndexerQueryPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "solrIndexerQueryPOS");
    }


    Integer getSolrIndexerQueryPOS()
    {
        return getSolrIndexerQueryPOS(getSession().getSessionContext());
    }


    int getSolrIndexerQueryPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSolrIndexerQueryPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSolrIndexerQueryPOSAsPrimitive()
    {
        return getSolrIndexerQueryPOSAsPrimitive(getSession().getSessionContext());
    }


    void setSolrIndexerQueryPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "solrIndexerQueryPOS", value);
    }


    void setSolrIndexerQueryPOS(Integer value)
    {
        setSolrIndexerQueryPOS(getSession().getSessionContext(), value);
    }


    void setSolrIndexerQueryPOS(SessionContext ctx, int value)
    {
        setSolrIndexerQueryPOS(ctx, Integer.valueOf(value));
    }


    void setSolrIndexerQueryPOS(int value)
    {
        setSolrIndexerQueryPOS(getSession().getSessionContext(), value);
    }


    public String getValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "value");
    }


    public String getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(String value)
    {
        setValue(getSession().getSessionContext(), value);
    }
}
