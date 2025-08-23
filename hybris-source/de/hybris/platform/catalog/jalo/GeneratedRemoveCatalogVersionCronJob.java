package de.hybris.platform.catalog.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRemoveCatalogVersionCronJob extends CronJob
{
    public static final String CATALOG = "catalog";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String DONTREMOVEOBJECTS = "dontRemoveObjects";
    public static final String NOTREMOVEDITEMS = "notRemovedItems";
    public static final String TOTALDELETEITEMCOUNT = "totalDeleteItemCount";
    public static final String CURRENTPROCESSINGITEMCOUNT = "currentProcessingItemCount";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("catalog", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("dontRemoveObjects", Item.AttributeMode.INITIAL);
        tmp.put("notRemovedItems", Item.AttributeMode.INITIAL);
        tmp.put("totalDeleteItemCount", Item.AttributeMode.INITIAL);
        tmp.put("currentProcessingItemCount", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Catalog getCatalog(SessionContext ctx)
    {
        return (Catalog)getProperty(ctx, "catalog");
    }


    public Catalog getCatalog()
    {
        return getCatalog(getSession().getSessionContext());
    }


    public void setCatalog(SessionContext ctx, Catalog value)
    {
        setProperty(ctx, "catalog", value);
    }


    public void setCatalog(Catalog value)
    {
        setCatalog(getSession().getSessionContext(), value);
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


    public Integer getCurrentProcessingItemCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "currentProcessingItemCount");
    }


    public Integer getCurrentProcessingItemCount()
    {
        return getCurrentProcessingItemCount(getSession().getSessionContext());
    }


    public int getCurrentProcessingItemCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getCurrentProcessingItemCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getCurrentProcessingItemCountAsPrimitive()
    {
        return getCurrentProcessingItemCountAsPrimitive(getSession().getSessionContext());
    }


    public void setCurrentProcessingItemCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "currentProcessingItemCount", value);
    }


    public void setCurrentProcessingItemCount(Integer value)
    {
        setCurrentProcessingItemCount(getSession().getSessionContext(), value);
    }


    public void setCurrentProcessingItemCount(SessionContext ctx, int value)
    {
        setCurrentProcessingItemCount(ctx, Integer.valueOf(value));
    }


    public void setCurrentProcessingItemCount(int value)
    {
        setCurrentProcessingItemCount(getSession().getSessionContext(), value);
    }


    public Boolean isDontRemoveObjects(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "dontRemoveObjects");
    }


    public Boolean isDontRemoveObjects()
    {
        return isDontRemoveObjects(getSession().getSessionContext());
    }


    public boolean isDontRemoveObjectsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDontRemoveObjects(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDontRemoveObjectsAsPrimitive()
    {
        return isDontRemoveObjectsAsPrimitive(getSession().getSessionContext());
    }


    public void setDontRemoveObjects(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "dontRemoveObjects", value);
    }


    public void setDontRemoveObjects(Boolean value)
    {
        setDontRemoveObjects(getSession().getSessionContext(), value);
    }


    public void setDontRemoveObjects(SessionContext ctx, boolean value)
    {
        setDontRemoveObjects(ctx, Boolean.valueOf(value));
    }


    public void setDontRemoveObjects(boolean value)
    {
        setDontRemoveObjects(getSession().getSessionContext(), value);
    }


    public ImpExMedia getNotRemovedItems(SessionContext ctx)
    {
        return (ImpExMedia)getProperty(ctx, "notRemovedItems");
    }


    public ImpExMedia getNotRemovedItems()
    {
        return getNotRemovedItems(getSession().getSessionContext());
    }


    public void setNotRemovedItems(SessionContext ctx, ImpExMedia value)
    {
        setProperty(ctx, "notRemovedItems", value);
    }


    public void setNotRemovedItems(ImpExMedia value)
    {
        setNotRemovedItems(getSession().getSessionContext(), value);
    }


    public Integer getTotalDeleteItemCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "totalDeleteItemCount");
    }


    public Integer getTotalDeleteItemCount()
    {
        return getTotalDeleteItemCount(getSession().getSessionContext());
    }


    public int getTotalDeleteItemCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getTotalDeleteItemCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getTotalDeleteItemCountAsPrimitive()
    {
        return getTotalDeleteItemCountAsPrimitive(getSession().getSessionContext());
    }


    public void setTotalDeleteItemCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "totalDeleteItemCount", value);
    }


    public void setTotalDeleteItemCount(Integer value)
    {
        setTotalDeleteItemCount(getSession().getSessionContext(), value);
    }


    public void setTotalDeleteItemCount(SessionContext ctx, int value)
    {
        setTotalDeleteItemCount(ctx, Integer.valueOf(value));
    }


    public void setTotalDeleteItemCount(int value)
    {
        setTotalDeleteItemCount(getSession().getSessionContext(), value);
    }
}
