package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSyncItemCronJob extends CronJob
{
    public static final String FORCEUPDATE = "forceUpdate";
    public static final String PENDINGITEMS = "pendingItems";
    public static final String FINISHEDITEMS = "finishedItems";
    public static final String CREATESAVEDVALUES = "createSavedValues";
    public static final String FULLSYNC = "fullSync";
    public static final String ABORTONCOLLIDINGSYNC = "abortOnCollidingSync";
    protected static final BidirectionalOneToManyHandler<GeneratedSyncItemCronJob> JOBHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.SYNCITEMJOB, false, "job", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("forceUpdate", Item.AttributeMode.INITIAL);
        tmp.put("createSavedValues", Item.AttributeMode.INITIAL);
        tmp.put("fullSync", Item.AttributeMode.INITIAL);
        tmp.put("abortOnCollidingSync", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAbortOnCollidingSync(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "abortOnCollidingSync");
    }


    public Boolean isAbortOnCollidingSync()
    {
        return isAbortOnCollidingSync(getSession().getSessionContext());
    }


    public boolean isAbortOnCollidingSyncAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAbortOnCollidingSync(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAbortOnCollidingSyncAsPrimitive()
    {
        return isAbortOnCollidingSyncAsPrimitive(getSession().getSessionContext());
    }


    public void setAbortOnCollidingSync(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "abortOnCollidingSync", value);
    }


    public void setAbortOnCollidingSync(Boolean value)
    {
        setAbortOnCollidingSync(getSession().getSessionContext(), value);
    }


    public void setAbortOnCollidingSync(SessionContext ctx, boolean value)
    {
        setAbortOnCollidingSync(ctx, Boolean.valueOf(value));
    }


    public void setAbortOnCollidingSync(boolean value)
    {
        setAbortOnCollidingSync(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        JOBHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Boolean isCreateSavedValues(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "createSavedValues");
    }


    public Boolean isCreateSavedValues()
    {
        return isCreateSavedValues(getSession().getSessionContext());
    }


    public boolean isCreateSavedValuesAsPrimitive(SessionContext ctx)
    {
        Boolean value = isCreateSavedValues(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCreateSavedValuesAsPrimitive()
    {
        return isCreateSavedValuesAsPrimitive(getSession().getSessionContext());
    }


    public void setCreateSavedValues(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "createSavedValues", value);
    }


    public void setCreateSavedValues(Boolean value)
    {
        setCreateSavedValues(getSession().getSessionContext(), value);
    }


    public void setCreateSavedValues(SessionContext ctx, boolean value)
    {
        setCreateSavedValues(ctx, Boolean.valueOf(value));
    }


    public void setCreateSavedValues(boolean value)
    {
        setCreateSavedValues(getSession().getSessionContext(), value);
    }


    public Collection<Item> getFinishedItems()
    {
        return getFinishedItems(getSession().getSessionContext());
    }


    public Boolean isForceUpdate(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "forceUpdate");
    }


    public Boolean isForceUpdate()
    {
        return isForceUpdate(getSession().getSessionContext());
    }


    public boolean isForceUpdateAsPrimitive(SessionContext ctx)
    {
        Boolean value = isForceUpdate(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isForceUpdateAsPrimitive()
    {
        return isForceUpdateAsPrimitive(getSession().getSessionContext());
    }


    public void setForceUpdate(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "forceUpdate", value);
    }


    public void setForceUpdate(Boolean value)
    {
        setForceUpdate(getSession().getSessionContext(), value);
    }


    public void setForceUpdate(SessionContext ctx, boolean value)
    {
        setForceUpdate(ctx, Boolean.valueOf(value));
    }


    public void setForceUpdate(boolean value)
    {
        setForceUpdate(getSession().getSessionContext(), value);
    }


    public Boolean isFullSync(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "fullSync");
    }


    public Boolean isFullSync()
    {
        return isFullSync(getSession().getSessionContext());
    }


    public boolean isFullSyncAsPrimitive(SessionContext ctx)
    {
        Boolean value = isFullSync(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isFullSyncAsPrimitive()
    {
        return isFullSyncAsPrimitive(getSession().getSessionContext());
    }


    public void setFullSync(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "fullSync", value);
    }


    public void setFullSync(Boolean value)
    {
        setFullSync(getSession().getSessionContext(), value);
    }


    public void setFullSync(SessionContext ctx, boolean value)
    {
        setFullSync(ctx, Boolean.valueOf(value));
    }


    public void setFullSync(boolean value)
    {
        setFullSync(getSession().getSessionContext(), value);
    }


    public Collection<Item> getPendingItems()
    {
        return getPendingItems(getSession().getSessionContext());
    }


    public void setPendingItems(Collection<Item> value)
    {
        setPendingItems(getSession().getSessionContext(), value);
    }


    public abstract Collection<Item> getFinishedItems(SessionContext paramSessionContext);


    public abstract Collection<Item> getPendingItems(SessionContext paramSessionContext);


    public abstract void setPendingItems(SessionContext paramSessionContext, Collection<Item> paramCollection);
}
