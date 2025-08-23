package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCompositeEntry extends GenericItem
{
    public static final String CODE = "code";
    public static final String EXECUTABLECRONJOB = "executableCronJob";
    public static final String TRIGGERABLEJOB = "triggerableJob";
    public static final String COMPOSITECRONJOBPOS = "compositeCronJobPOS";
    public static final String COMPOSITECRONJOB = "compositeCronJob";
    protected static final BidirectionalOneToManyHandler<GeneratedCompositeEntry> COMPOSITECRONJOBHANDLER = new BidirectionalOneToManyHandler(GeneratedProcessingConstants.TC.COMPOSITEENTRY, false, "compositeCronJob", "compositeCronJobPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("executableCronJob", Item.AttributeMode.INITIAL);
        tmp.put("triggerableJob", Item.AttributeMode.INITIAL);
        tmp.put("compositeCronJobPOS", Item.AttributeMode.INITIAL);
        tmp.put("compositeCronJob", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public CompositeCronJob getCompositeCronJob(SessionContext ctx)
    {
        return (CompositeCronJob)getProperty(ctx, "compositeCronJob");
    }


    public CompositeCronJob getCompositeCronJob()
    {
        return getCompositeCronJob(getSession().getSessionContext());
    }


    public void setCompositeCronJob(SessionContext ctx, CompositeCronJob value)
    {
        COMPOSITECRONJOBHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCompositeCronJob(CompositeCronJob value)
    {
        setCompositeCronJob(getSession().getSessionContext(), value);
    }


    Integer getCompositeCronJobPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "compositeCronJobPOS");
    }


    Integer getCompositeCronJobPOS()
    {
        return getCompositeCronJobPOS(getSession().getSessionContext());
    }


    int getCompositeCronJobPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getCompositeCronJobPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getCompositeCronJobPOSAsPrimitive()
    {
        return getCompositeCronJobPOSAsPrimitive(getSession().getSessionContext());
    }


    void setCompositeCronJobPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "compositeCronJobPOS", value);
    }


    void setCompositeCronJobPOS(Integer value)
    {
        setCompositeCronJobPOS(getSession().getSessionContext(), value);
    }


    void setCompositeCronJobPOS(SessionContext ctx, int value)
    {
        setCompositeCronJobPOS(ctx, Integer.valueOf(value));
    }


    void setCompositeCronJobPOS(int value)
    {
        setCompositeCronJobPOS(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        COMPOSITECRONJOBHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public CronJob getExecutableCronJob(SessionContext ctx)
    {
        return (CronJob)getProperty(ctx, "executableCronJob");
    }


    public CronJob getExecutableCronJob()
    {
        return getExecutableCronJob(getSession().getSessionContext());
    }


    public void setExecutableCronJob(SessionContext ctx, CronJob value)
    {
        setProperty(ctx, "executableCronJob", value);
    }


    public void setExecutableCronJob(CronJob value)
    {
        setExecutableCronJob(getSession().getSessionContext(), value);
    }


    public Job getTriggerableJob(SessionContext ctx)
    {
        return (Job)getProperty(ctx, "triggerableJob");
    }


    public Job getTriggerableJob()
    {
        return getTriggerableJob(getSession().getSessionContext());
    }


    public void setTriggerableJob(SessionContext ctx, Job value)
    {
        setProperty(ctx, "triggerableJob", value);
    }


    public void setTriggerableJob(Job value)
    {
        setTriggerableJob(getSession().getSessionContext(), value);
    }
}
