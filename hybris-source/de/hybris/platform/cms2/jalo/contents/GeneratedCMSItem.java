package de.hybris.platform.cms2.jalo.contents;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCMSItem extends GenericItem
{
    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String SYNCHRONIZATIONBLOCKED = "synchronizationBlocked";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("uid", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("synchronizationBlocked", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "catalogVersion");
    }


    public CatalogVersion getCatalogVersion()
    {
        return getCatalogVersion(getSession().getSessionContext());
    }


    public void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "catalogVersion", value);
    }


    public void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
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


    public Boolean isSynchronizationBlocked(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "synchronizationBlocked");
    }


    public Boolean isSynchronizationBlocked()
    {
        return isSynchronizationBlocked(getSession().getSessionContext());
    }


    public boolean isSynchronizationBlockedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSynchronizationBlocked(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSynchronizationBlockedAsPrimitive()
    {
        return isSynchronizationBlockedAsPrimitive(getSession().getSessionContext());
    }


    public void setSynchronizationBlocked(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "synchronizationBlocked", value);
    }


    public void setSynchronizationBlocked(Boolean value)
    {
        setSynchronizationBlocked(getSession().getSessionContext(), value);
    }


    public void setSynchronizationBlocked(SessionContext ctx, boolean value)
    {
        setSynchronizationBlocked(ctx, Boolean.valueOf(value));
    }


    public void setSynchronizationBlocked(boolean value)
    {
        setSynchronizationBlocked(getSession().getSessionContext(), value);
    }


    public String getUid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uid");
    }


    public String getUid()
    {
        return getUid(getSession().getSessionContext());
    }


    public void setUid(SessionContext ctx, String value)
    {
        setProperty(ctx, "uid", value);
    }


    public void setUid(String value)
    {
        setUid(getSession().getSessionContext(), value);
    }
}
