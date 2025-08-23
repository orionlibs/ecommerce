package de.hybris.platform.solrfacetsearch.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.constants.GeneratedSolrfacetsearchConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrIndexOperation extends GenericItem
{
    public static final String ID = "id";
    public static final String OPERATION = "operation";
    public static final String EXTERNAL = "external";
    public static final String STATUS = "status";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String INDEX = "index";
    protected static final BidirectionalOneToManyHandler<GeneratedSolrIndexOperation> INDEXHANDLER = new BidirectionalOneToManyHandler(GeneratedSolrfacetsearchConstants.TC.SOLRINDEXOPERATION, false, "index", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("id", Item.AttributeMode.INITIAL);
        tmp.put("operation", Item.AttributeMode.INITIAL);
        tmp.put("external", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("startTime", Item.AttributeMode.INITIAL);
        tmp.put("endTime", Item.AttributeMode.INITIAL);
        tmp.put("index", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        INDEXHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Date getEndTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "endTime");
    }


    public Date getEndTime()
    {
        return getEndTime(getSession().getSessionContext());
    }


    public void setEndTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "endTime", value);
    }


    public void setEndTime(Date value)
    {
        setEndTime(getSession().getSessionContext(), value);
    }


    public Boolean isExternal(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "external");
    }


    public Boolean isExternal()
    {
        return isExternal(getSession().getSessionContext());
    }


    public boolean isExternalAsPrimitive(SessionContext ctx)
    {
        Boolean value = isExternal(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isExternalAsPrimitive()
    {
        return isExternalAsPrimitive(getSession().getSessionContext());
    }


    public void setExternal(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "external", value);
    }


    public void setExternal(Boolean value)
    {
        setExternal(getSession().getSessionContext(), value);
    }


    public void setExternal(SessionContext ctx, boolean value)
    {
        setExternal(ctx, Boolean.valueOf(value));
    }


    public void setExternal(boolean value)
    {
        setExternal(getSession().getSessionContext(), value);
    }


    public Long getId(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "id");
    }


    public Long getId()
    {
        return getId(getSession().getSessionContext());
    }


    public long getIdAsPrimitive(SessionContext ctx)
    {
        Long value = getId(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getIdAsPrimitive()
    {
        return getIdAsPrimitive(getSession().getSessionContext());
    }


    protected void setId(SessionContext ctx, Long value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'id' is not changeable", 0);
        }
        setProperty(ctx, "id", value);
    }


    protected void setId(Long value)
    {
        setId(getSession().getSessionContext(), value);
    }


    protected void setId(SessionContext ctx, long value)
    {
        setId(ctx, Long.valueOf(value));
    }


    protected void setId(long value)
    {
        setId(getSession().getSessionContext(), value);
    }


    public SolrIndex getIndex(SessionContext ctx)
    {
        return (SolrIndex)getProperty(ctx, "index");
    }


    public SolrIndex getIndex()
    {
        return getIndex(getSession().getSessionContext());
    }


    protected void setIndex(SessionContext ctx, SolrIndex value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'index' is not changeable", 0);
        }
        INDEXHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setIndex(SolrIndex value)
    {
        setIndex(getSession().getSessionContext(), value);
    }


    public EnumerationValue getOperation(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "operation");
    }


    public EnumerationValue getOperation()
    {
        return getOperation(getSession().getSessionContext());
    }


    protected void setOperation(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'operation' is not changeable", 0);
        }
        setProperty(ctx, "operation", value);
    }


    protected void setOperation(EnumerationValue value)
    {
        setOperation(getSession().getSessionContext(), value);
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
}
