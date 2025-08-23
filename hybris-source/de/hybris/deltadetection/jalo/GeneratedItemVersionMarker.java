package de.hybris.deltadetection.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedItemVersionMarker extends GenericItem
{
    public static final String ITEMPK = "itemPK";
    public static final String ITEMCOMPOSEDTYPE = "itemComposedType";
    public static final String VERSIONTS = "versionTS";
    public static final String VERSIONVALUE = "versionValue";
    public static final String LASTVERSIONVALUE = "lastVersionValue";
    public static final String INFO = "info";
    public static final String STREAMID = "streamId";
    public static final String STATUS = "status";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("itemPK", Item.AttributeMode.INITIAL);
        tmp.put("itemComposedType", Item.AttributeMode.INITIAL);
        tmp.put("versionTS", Item.AttributeMode.INITIAL);
        tmp.put("versionValue", Item.AttributeMode.INITIAL);
        tmp.put("lastVersionValue", Item.AttributeMode.INITIAL);
        tmp.put("info", Item.AttributeMode.INITIAL);
        tmp.put("streamId", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getInfo(SessionContext ctx)
    {
        return (String)getProperty(ctx, "info");
    }


    public String getInfo()
    {
        return getInfo(getSession().getSessionContext());
    }


    public void setInfo(SessionContext ctx, String value)
    {
        setProperty(ctx, "info", value);
    }


    public void setInfo(String value)
    {
        setInfo(getSession().getSessionContext(), value);
    }


    public ComposedType getItemComposedType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "itemComposedType");
    }


    public ComposedType getItemComposedType()
    {
        return getItemComposedType(getSession().getSessionContext());
    }


    protected void setItemComposedType(SessionContext ctx, ComposedType value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'itemComposedType' is not changeable", 0);
        }
        setProperty(ctx, "itemComposedType", value);
    }


    protected void setItemComposedType(ComposedType value)
    {
        setItemComposedType(getSession().getSessionContext(), value);
    }


    public Long getItemPK(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "itemPK");
    }


    public Long getItemPK()
    {
        return getItemPK(getSession().getSessionContext());
    }


    public long getItemPKAsPrimitive(SessionContext ctx)
    {
        Long value = getItemPK(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getItemPKAsPrimitive()
    {
        return getItemPKAsPrimitive(getSession().getSessionContext());
    }


    protected void setItemPK(SessionContext ctx, Long value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'itemPK' is not changeable", 0);
        }
        setProperty(ctx, "itemPK", value);
    }


    protected void setItemPK(Long value)
    {
        setItemPK(getSession().getSessionContext(), value);
    }


    protected void setItemPK(SessionContext ctx, long value)
    {
        setItemPK(ctx, Long.valueOf(value));
    }


    protected void setItemPK(long value)
    {
        setItemPK(getSession().getSessionContext(), value);
    }


    public String getLastVersionValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "lastVersionValue");
    }


    public String getLastVersionValue()
    {
        return getLastVersionValue(getSession().getSessionContext());
    }


    public void setLastVersionValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "lastVersionValue", value);
    }


    public void setLastVersionValue(String value)
    {
        setLastVersionValue(getSession().getSessionContext(), value);
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


    public String getStreamId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "streamId");
    }


    public String getStreamId()
    {
        return getStreamId(getSession().getSessionContext());
    }


    protected void setStreamId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'streamId' is not changeable", 0);
        }
        setProperty(ctx, "streamId", value);
    }


    protected void setStreamId(String value)
    {
        setStreamId(getSession().getSessionContext(), value);
    }


    public Date getVersionTS(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "versionTS");
    }


    public Date getVersionTS()
    {
        return getVersionTS(getSession().getSessionContext());
    }


    public void setVersionTS(SessionContext ctx, Date value)
    {
        setProperty(ctx, "versionTS", value);
    }


    public void setVersionTS(Date value)
    {
        setVersionTS(getSession().getSessionContext(), value);
    }


    public String getVersionValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "versionValue");
    }


    public String getVersionValue()
    {
        return getVersionValue(getSession().getSessionContext());
    }


    public void setVersionValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "versionValue", value);
    }


    public void setVersionValue(String value)
    {
        setVersionValue(getSession().getSessionContext(), value);
    }
}
