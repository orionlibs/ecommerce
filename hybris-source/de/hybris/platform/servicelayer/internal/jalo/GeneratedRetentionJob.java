package de.hybris.platform.servicelayer.internal.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.processing.jalo.AbstractRetentionRule;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRetentionJob extends ServicelayerJob
{
    public static final String RETENTIONRULE = "retentionRule";
    public static final String BATCHSIZE = "batchSize";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(ServicelayerJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("retentionRule", Item.AttributeMode.INITIAL);
        tmp.put("batchSize", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getBatchSize(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "batchSize");
    }


    public Integer getBatchSize()
    {
        return getBatchSize(getSession().getSessionContext());
    }


    public int getBatchSizeAsPrimitive(SessionContext ctx)
    {
        Integer value = getBatchSize(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getBatchSizeAsPrimitive()
    {
        return getBatchSizeAsPrimitive(getSession().getSessionContext());
    }


    public void setBatchSize(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "batchSize", value);
    }


    public void setBatchSize(Integer value)
    {
        setBatchSize(getSession().getSessionContext(), value);
    }


    public void setBatchSize(SessionContext ctx, int value)
    {
        setBatchSize(ctx, Integer.valueOf(value));
    }


    public void setBatchSize(int value)
    {
        setBatchSize(getSession().getSessionContext(), value);
    }


    public AbstractRetentionRule getRetentionRule(SessionContext ctx)
    {
        return (AbstractRetentionRule)getProperty(ctx, "retentionRule");
    }


    public AbstractRetentionRule getRetentionRule()
    {
        return getRetentionRule(getSession().getSessionContext());
    }


    public void setRetentionRule(SessionContext ctx, AbstractRetentionRule value)
    {
        setProperty(ctx, "retentionRule", value);
    }


    public void setRetentionRule(AbstractRetentionRule value)
    {
        setRetentionRule(getSession().getSessionContext(), value);
    }
}
