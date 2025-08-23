package de.hybris.platform.catalog.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedItemSyncTimestamp extends GenericItem
{
    public static final String SYNCJOB = "syncJob";
    public static final String SOURCEITEM = "sourceItem";
    public static final String TARGETITEM = "targetItem";
    public static final String SOURCEVERSION = "sourceVersion";
    public static final String TARGETVERSION = "targetVersion";
    public static final String LASTSYNCSOURCEMODIFIEDTIME = "lastSyncSourceModifiedTime";
    public static final String LASTSYNCTIME = "lastSyncTime";
    public static final String PENDINGATTRIBUTESOWNERJOB = "pendingAttributesOwnerJob";
    public static final String PENDINGATTRIBUTESSCHEDULEDTURN = "pendingAttributesScheduledTurn";
    public static final String PENDINGATTRIBUTEQUALIFIERS = "pendingAttributeQualifiers";
    public static final String OUTDATED = "outdated";
    public static final String PENDINGATTRIBUTES = "pendingAttributes";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("syncJob", Item.AttributeMode.INITIAL);
        tmp.put("sourceItem", Item.AttributeMode.INITIAL);
        tmp.put("targetItem", Item.AttributeMode.INITIAL);
        tmp.put("sourceVersion", Item.AttributeMode.INITIAL);
        tmp.put("targetVersion", Item.AttributeMode.INITIAL);
        tmp.put("lastSyncSourceModifiedTime", Item.AttributeMode.INITIAL);
        tmp.put("lastSyncTime", Item.AttributeMode.INITIAL);
        tmp.put("pendingAttributesOwnerJob", Item.AttributeMode.INITIAL);
        tmp.put("pendingAttributesScheduledTurn", Item.AttributeMode.INITIAL);
        tmp.put("pendingAttributeQualifiers", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Date getLastSyncSourceModifiedTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "lastSyncSourceModifiedTime");
    }


    public Date getLastSyncSourceModifiedTime()
    {
        return getLastSyncSourceModifiedTime(getSession().getSessionContext());
    }


    public void setLastSyncSourceModifiedTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "lastSyncSourceModifiedTime", value);
    }


    public void setLastSyncSourceModifiedTime(Date value)
    {
        setLastSyncSourceModifiedTime(getSession().getSessionContext(), value);
    }


    public Date getLastSyncTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "lastSyncTime");
    }


    public Date getLastSyncTime()
    {
        return getLastSyncTime(getSession().getSessionContext());
    }


    public void setLastSyncTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "lastSyncTime", value);
    }


    public void setLastSyncTime(Date value)
    {
        setLastSyncTime(getSession().getSessionContext(), value);
    }


    public Boolean isOutdated()
    {
        return isOutdated(getSession().getSessionContext());
    }


    public boolean isOutdatedAsPrimitive(SessionContext ctx)
    {
        Boolean value = isOutdated(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isOutdatedAsPrimitive()
    {
        return isOutdatedAsPrimitive(getSession().getSessionContext());
    }


    public String getPendingAttributeQualifiers(SessionContext ctx)
    {
        return (String)getProperty(ctx, "pendingAttributeQualifiers");
    }


    public String getPendingAttributeQualifiers()
    {
        return getPendingAttributeQualifiers(getSession().getSessionContext());
    }


    public void setPendingAttributeQualifiers(SessionContext ctx, String value)
    {
        setProperty(ctx, "pendingAttributeQualifiers", value);
    }


    public void setPendingAttributeQualifiers(String value)
    {
        setPendingAttributeQualifiers(getSession().getSessionContext(), value);
    }


    public Collection<AttributeDescriptor> getPendingAttributes()
    {
        return getPendingAttributes(getSession().getSessionContext());
    }


    public SyncItemCronJob getPendingAttributesOwnerJob(SessionContext ctx)
    {
        return (SyncItemCronJob)getProperty(ctx, "pendingAttributesOwnerJob");
    }


    public SyncItemCronJob getPendingAttributesOwnerJob()
    {
        return getPendingAttributesOwnerJob(getSession().getSessionContext());
    }


    public void setPendingAttributesOwnerJob(SessionContext ctx, SyncItemCronJob value)
    {
        setProperty(ctx, "pendingAttributesOwnerJob", value);
    }


    public void setPendingAttributesOwnerJob(SyncItemCronJob value)
    {
        setPendingAttributesOwnerJob(getSession().getSessionContext(), value);
    }


    public Integer getPendingAttributesScheduledTurn(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "pendingAttributesScheduledTurn");
    }


    public Integer getPendingAttributesScheduledTurn()
    {
        return getPendingAttributesScheduledTurn(getSession().getSessionContext());
    }


    public int getPendingAttributesScheduledTurnAsPrimitive(SessionContext ctx)
    {
        Integer value = getPendingAttributesScheduledTurn(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPendingAttributesScheduledTurnAsPrimitive()
    {
        return getPendingAttributesScheduledTurnAsPrimitive(getSession().getSessionContext());
    }


    public void setPendingAttributesScheduledTurn(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "pendingAttributesScheduledTurn", value);
    }


    public void setPendingAttributesScheduledTurn(Integer value)
    {
        setPendingAttributesScheduledTurn(getSession().getSessionContext(), value);
    }


    public void setPendingAttributesScheduledTurn(SessionContext ctx, int value)
    {
        setPendingAttributesScheduledTurn(ctx, Integer.valueOf(value));
    }


    public void setPendingAttributesScheduledTurn(int value)
    {
        setPendingAttributesScheduledTurn(getSession().getSessionContext(), value);
    }


    public Item getSourceItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "sourceItem");
    }


    public Item getSourceItem()
    {
        return getSourceItem(getSession().getSessionContext());
    }


    protected void setSourceItem(SessionContext ctx, Item value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'sourceItem' is not changeable", 0);
        }
        setProperty(ctx, "sourceItem", value);
    }


    protected void setSourceItem(Item value)
    {
        setSourceItem(getSession().getSessionContext(), value);
    }


    public CatalogVersion getSourceVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "sourceVersion");
    }


    public CatalogVersion getSourceVersion()
    {
        return getSourceVersion(getSession().getSessionContext());
    }


    protected void setSourceVersion(SessionContext ctx, CatalogVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'sourceVersion' is not changeable", 0);
        }
        setProperty(ctx, "sourceVersion", value);
    }


    protected void setSourceVersion(CatalogVersion value)
    {
        setSourceVersion(getSession().getSessionContext(), value);
    }


    public SyncItemJob getSyncJob(SessionContext ctx)
    {
        return (SyncItemJob)getProperty(ctx, "syncJob");
    }


    public SyncItemJob getSyncJob()
    {
        return getSyncJob(getSession().getSessionContext());
    }


    protected void setSyncJob(SessionContext ctx, SyncItemJob value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'syncJob' is not changeable", 0);
        }
        setProperty(ctx, "syncJob", value);
    }


    protected void setSyncJob(SyncItemJob value)
    {
        setSyncJob(getSession().getSessionContext(), value);
    }


    public Item getTargetItem(SessionContext ctx)
    {
        return (Item)getProperty(ctx, "targetItem");
    }


    public Item getTargetItem()
    {
        return getTargetItem(getSession().getSessionContext());
    }


    protected void setTargetItem(SessionContext ctx, Item value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'targetItem' is not changeable", 0);
        }
        setProperty(ctx, "targetItem", value);
    }


    protected void setTargetItem(Item value)
    {
        setTargetItem(getSession().getSessionContext(), value);
    }


    public CatalogVersion getTargetVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "targetVersion");
    }


    public CatalogVersion getTargetVersion()
    {
        return getTargetVersion(getSession().getSessionContext());
    }


    protected void setTargetVersion(SessionContext ctx, CatalogVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'targetVersion' is not changeable", 0);
        }
        setProperty(ctx, "targetVersion", value);
    }


    protected void setTargetVersion(CatalogVersion value)
    {
        setTargetVersion(getSession().getSessionContext(), value);
    }


    public abstract Boolean isOutdated(SessionContext paramSessionContext);


    public abstract Collection<AttributeDescriptor> getPendingAttributes(SessionContext paramSessionContext);
}
