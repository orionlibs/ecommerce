package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCatalogVersionSyncCronJob extends SyncItemCronJob
{
    public static final String STATUSMESSAGE = "statusMessage";
    public static final String SCHEDULEMEDIAS = "scheduleMedias";
    protected static final OneToManyHandler<CatalogVersionSyncScheduleMedia> SCHEDULEMEDIASHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.CATALOGVERSIONSYNCSCHEDULEMEDIA, true, "cronjob", "cronjobPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SyncItemCronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("statusMessage", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<CatalogVersionSyncScheduleMedia> getScheduleMedias(SessionContext ctx)
    {
        return (List<CatalogVersionSyncScheduleMedia>)SCHEDULEMEDIASHANDLER.getValues(ctx, (Item)this);
    }


    public List<CatalogVersionSyncScheduleMedia> getScheduleMedias()
    {
        return getScheduleMedias(getSession().getSessionContext());
    }


    public void setScheduleMedias(SessionContext ctx, List<CatalogVersionSyncScheduleMedia> value)
    {
        SCHEDULEMEDIASHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setScheduleMedias(List<CatalogVersionSyncScheduleMedia> value)
    {
        setScheduleMedias(getSession().getSessionContext(), value);
    }


    public void addToScheduleMedias(SessionContext ctx, CatalogVersionSyncScheduleMedia value)
    {
        SCHEDULEMEDIASHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToScheduleMedias(CatalogVersionSyncScheduleMedia value)
    {
        addToScheduleMedias(getSession().getSessionContext(), value);
    }


    public void removeFromScheduleMedias(SessionContext ctx, CatalogVersionSyncScheduleMedia value)
    {
        SCHEDULEMEDIASHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromScheduleMedias(CatalogVersionSyncScheduleMedia value)
    {
        removeFromScheduleMedias(getSession().getSessionContext(), value);
    }


    public String getStatusMessage(SessionContext ctx)
    {
        return (String)getProperty(ctx, "statusMessage");
    }


    public String getStatusMessage()
    {
        return getStatusMessage(getSession().getSessionContext());
    }


    public void setStatusMessage(SessionContext ctx, String value)
    {
        setProperty(ctx, "statusMessage", value);
    }


    public void setStatusMessage(String value)
    {
        setStatusMessage(getSession().getSessionContext(), value);
    }
}
