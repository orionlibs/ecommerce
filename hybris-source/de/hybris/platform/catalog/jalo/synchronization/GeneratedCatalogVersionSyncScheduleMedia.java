package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCatalogVersionSyncScheduleMedia extends Media
{
    public static final String SCHEDULEDCOUNT = "scheduledCount";
    public static final String CRONJOBPOS = "cronjobPOS";
    public static final String CRONJOB = "cronjob";
    protected static final BidirectionalOneToManyHandler<GeneratedCatalogVersionSyncScheduleMedia> CRONJOBHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.CATALOGVERSIONSYNCSCHEDULEMEDIA, false, "cronjob", "cronjobPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Media.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("scheduledCount", Item.AttributeMode.INITIAL);
        tmp.put("cronjobPOS", Item.AttributeMode.INITIAL);
        tmp.put("cronjob", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CRONJOBHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public CatalogVersionSyncCronJob getCronjob(SessionContext ctx)
    {
        return (CatalogVersionSyncCronJob)getProperty(ctx, "cronjob");
    }


    public CatalogVersionSyncCronJob getCronjob()
    {
        return getCronjob(getSession().getSessionContext());
    }


    protected void setCronjob(SessionContext ctx, CatalogVersionSyncCronJob value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'cronjob' is not changeable", 0);
        }
        CRONJOBHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setCronjob(CatalogVersionSyncCronJob value)
    {
        setCronjob(getSession().getSessionContext(), value);
    }


    Integer getCronjobPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "cronjobPOS");
    }


    Integer getCronjobPOS()
    {
        return getCronjobPOS(getSession().getSessionContext());
    }


    int getCronjobPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getCronjobPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getCronjobPOSAsPrimitive()
    {
        return getCronjobPOSAsPrimitive(getSession().getSessionContext());
    }


    void setCronjobPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "cronjobPOS", value);
    }


    void setCronjobPOS(Integer value)
    {
        setCronjobPOS(getSession().getSessionContext(), value);
    }


    void setCronjobPOS(SessionContext ctx, int value)
    {
        setCronjobPOS(ctx, Integer.valueOf(value));
    }


    void setCronjobPOS(int value)
    {
        setCronjobPOS(getSession().getSessionContext(), value);
    }


    public Integer getScheduledCount(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "scheduledCount");
    }


    public Integer getScheduledCount()
    {
        return getScheduledCount(getSession().getSessionContext());
    }


    public int getScheduledCountAsPrimitive(SessionContext ctx)
    {
        Integer value = getScheduledCount(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getScheduledCountAsPrimitive()
    {
        return getScheduledCountAsPrimitive(getSession().getSessionContext());
    }


    public void setScheduledCount(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "scheduledCount", value);
    }


    public void setScheduledCount(Integer value)
    {
        setScheduledCount(getSession().getSessionContext(), value);
    }


    public void setScheduledCount(SessionContext ctx, int value)
    {
        setScheduledCount(ctx, Integer.valueOf(value));
    }


    public void setScheduledCount(int value)
    {
        setScheduledCount(getSession().getSessionContext(), value);
    }
}
