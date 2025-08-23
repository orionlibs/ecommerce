package de.hybris.platform.jalo.media;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAbstractMedia extends GenericItem
{
    public static final String MIME = "mime";
    public static final String SIZE = "size";
    public static final String DATAPK = "dataPK";
    public static final String LOCATION = "location";
    public static final String LOCATIONHASH = "locationHash";
    public static final String REALFILENAME = "realFileName";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("mime", Item.AttributeMode.INITIAL);
        tmp.put("size", Item.AttributeMode.INITIAL);
        tmp.put("dataPK", Item.AttributeMode.INITIAL);
        tmp.put("location", Item.AttributeMode.INITIAL);
        tmp.put("locationHash", Item.AttributeMode.INITIAL);
        tmp.put("realFileName", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Long getDataPK(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "dataPK");
    }


    public Long getDataPK()
    {
        return getDataPK(getSession().getSessionContext());
    }


    public long getDataPKAsPrimitive(SessionContext ctx)
    {
        Long value = getDataPK(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getDataPKAsPrimitive()
    {
        return getDataPKAsPrimitive(getSession().getSessionContext());
    }


    public void setDataPK(SessionContext ctx, Long value)
    {
        setProperty(ctx, "dataPK", value);
    }


    public void setDataPK(Long value)
    {
        setDataPK(getSession().getSessionContext(), value);
    }


    public void setDataPK(SessionContext ctx, long value)
    {
        setDataPK(ctx, Long.valueOf(value));
    }


    public void setDataPK(long value)
    {
        setDataPK(getSession().getSessionContext(), value);
    }


    public String getLocation(SessionContext ctx)
    {
        return (String)getProperty(ctx, "location");
    }


    public String getLocation()
    {
        return getLocation(getSession().getSessionContext());
    }


    public void setLocation(SessionContext ctx, String value)
    {
        setProperty(ctx, "location", value);
    }


    public void setLocation(String value)
    {
        setLocation(getSession().getSessionContext(), value);
    }


    public String getLocationHash(SessionContext ctx)
    {
        return (String)getProperty(ctx, "locationHash");
    }


    public String getLocationHash()
    {
        return getLocationHash(getSession().getSessionContext());
    }


    public void setLocationHash(SessionContext ctx, String value)
    {
        setProperty(ctx, "locationHash", value);
    }


    public void setLocationHash(String value)
    {
        setLocationHash(getSession().getSessionContext(), value);
    }


    public String getMime(SessionContext ctx)
    {
        return (String)getProperty(ctx, "mime");
    }


    public String getMime()
    {
        return getMime(getSession().getSessionContext());
    }


    public void setMime(SessionContext ctx, String value) throws JaloBusinessException
    {
        setProperty(ctx, "mime", value);
    }


    public void setMime(String value) throws JaloBusinessException
    {
        setMime(getSession().getSessionContext(), value);
    }


    public String getRealFileName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "realFileName");
    }


    public String getRealFileName()
    {
        return getRealFileName(getSession().getSessionContext());
    }


    public void setRealFileName(SessionContext ctx, String value)
    {
        setProperty(ctx, "realFileName", value);
    }


    public void setRealFileName(String value)
    {
        setRealFileName(getSession().getSessionContext(), value);
    }


    public Long getSize(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "size");
    }


    public Long getSize()
    {
        return getSize(getSession().getSessionContext());
    }


    public long getSizeAsPrimitive(SessionContext ctx)
    {
        Long value = getSize(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getSizeAsPrimitive()
    {
        return getSizeAsPrimitive(getSession().getSessionContext());
    }


    public void setSize(SessionContext ctx, Long value)
    {
        setProperty(ctx, "size", value);
    }


    public void setSize(Long value)
    {
        setSize(getSession().getSessionContext(), value);
    }


    public void setSize(SessionContext ctx, long value)
    {
        setSize(ctx, Long.valueOf(value));
    }


    public void setSize(long value)
    {
        setSize(getSession().getSessionContext(), value);
    }
}
