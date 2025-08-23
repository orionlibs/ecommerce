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

public abstract class GeneratedSolrValueRange extends GenericItem
{
    public static final String NAME = "name";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String SOLRVALUERANGESETPOS = "solrValueRangeSetPOS";
    public static final String SOLRVALUERANGESET = "solrValueRangeSet";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrValueRange> SOLRVALUERANGESETHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRVALUERANGE, false, "solrValueRangeSet", "solrValueRangeSetPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("from", Item.AttributeMode.INITIAL);
        tmp.put("to", Item.AttributeMode.INITIAL);
        tmp.put("solrValueRangeSetPOS", Item.AttributeMode.INITIAL);
        tmp.put("solrValueRangeSet", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        SOLRVALUERANGESETHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getFrom(SessionContext ctx)
    {
        return (String)getProperty(ctx, "from");
    }


    public String getFrom()
    {
        return getFrom(getSession().getSessionContext());
    }


    public void setFrom(SessionContext ctx, String value)
    {
        setProperty(ctx, "from", value);
    }


    public void setFrom(String value)
    {
        setFrom(getSession().getSessionContext(), value);
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


    public SolrValueRangeSet getSolrValueRangeSet(SessionContext ctx)
    {
        return (SolrValueRangeSet)getProperty(ctx, "solrValueRangeSet");
    }


    public SolrValueRangeSet getSolrValueRangeSet()
    {
        return getSolrValueRangeSet(getSession().getSessionContext());
    }


    public void setSolrValueRangeSet(SessionContext ctx, SolrValueRangeSet value)
    {
        SOLRVALUERANGESETHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setSolrValueRangeSet(SolrValueRangeSet value)
    {
        setSolrValueRangeSet(getSession().getSessionContext(), value);
    }


    Integer getSolrValueRangeSetPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "solrValueRangeSetPOS");
    }


    Integer getSolrValueRangeSetPOS()
    {
        return getSolrValueRangeSetPOS(getSession().getSessionContext());
    }


    int getSolrValueRangeSetPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getSolrValueRangeSetPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getSolrValueRangeSetPOSAsPrimitive()
    {
        return getSolrValueRangeSetPOSAsPrimitive(getSession().getSessionContext());
    }


    void setSolrValueRangeSetPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "solrValueRangeSetPOS", value);
    }


    void setSolrValueRangeSetPOS(Integer value)
    {
        setSolrValueRangeSetPOS(getSession().getSessionContext(), value);
    }


    void setSolrValueRangeSetPOS(SessionContext ctx, int value)
    {
        setSolrValueRangeSetPOS(ctx, Integer.valueOf(value));
    }


    void setSolrValueRangeSetPOS(int value)
    {
        setSolrValueRangeSetPOS(getSession().getSessionContext(), value);
    }


    public String getTo(SessionContext ctx)
    {
        return (String)getProperty(ctx, "to");
    }


    public String getTo()
    {
        return getTo(getSession().getSessionContext());
    }


    public void setTo(SessionContext ctx, String value)
    {
        setProperty(ctx, "to", value);
    }


    public void setTo(String value)
    {
        setTo(getSession().getSessionContext(), value);
    }
}
