package de.hybris.platform.processing.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedAfterRetentionCleanupRule extends AbstractRetentionRule
{
    public static final String RETIREMENTITEMTYPE = "retirementItemType";
    public static final String RETIREMENTDATEATTRIBUTE = "retirementDateAttribute";
    public static final String RETENTIONTIMESECONDS = "retentionTimeSeconds";
    public static final String ITEMFILTEREXPRESSION = "itemFilterExpression";
    public static final String RETIREMENTDATEEXPRESSION = "retirementDateExpression";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRetentionRule.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("retirementItemType", Item.AttributeMode.INITIAL);
        tmp.put("retirementDateAttribute", Item.AttributeMode.INITIAL);
        tmp.put("retentionTimeSeconds", Item.AttributeMode.INITIAL);
        tmp.put("itemFilterExpression", Item.AttributeMode.INITIAL);
        tmp.put("retirementDateExpression", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getItemFilterExpression(SessionContext ctx)
    {
        return (String)getProperty(ctx, "itemFilterExpression");
    }


    public String getItemFilterExpression()
    {
        return getItemFilterExpression(getSession().getSessionContext());
    }


    public void setItemFilterExpression(SessionContext ctx, String value)
    {
        setProperty(ctx, "itemFilterExpression", value);
    }


    public void setItemFilterExpression(String value)
    {
        setItemFilterExpression(getSession().getSessionContext(), value);
    }


    public Long getRetentionTimeSeconds(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "retentionTimeSeconds");
    }


    public Long getRetentionTimeSeconds()
    {
        return getRetentionTimeSeconds(getSession().getSessionContext());
    }


    public long getRetentionTimeSecondsAsPrimitive(SessionContext ctx)
    {
        Long value = getRetentionTimeSeconds(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getRetentionTimeSecondsAsPrimitive()
    {
        return getRetentionTimeSecondsAsPrimitive(getSession().getSessionContext());
    }


    public void setRetentionTimeSeconds(SessionContext ctx, Long value)
    {
        setProperty(ctx, "retentionTimeSeconds", value);
    }


    public void setRetentionTimeSeconds(Long value)
    {
        setRetentionTimeSeconds(getSession().getSessionContext(), value);
    }


    public void setRetentionTimeSeconds(SessionContext ctx, long value)
    {
        setRetentionTimeSeconds(ctx, Long.valueOf(value));
    }


    public void setRetentionTimeSeconds(long value)
    {
        setRetentionTimeSeconds(getSession().getSessionContext(), value);
    }


    public AttributeDescriptor getRetirementDateAttribute(SessionContext ctx)
    {
        return (AttributeDescriptor)getProperty(ctx, "retirementDateAttribute");
    }


    public AttributeDescriptor getRetirementDateAttribute()
    {
        return getRetirementDateAttribute(getSession().getSessionContext());
    }


    public void setRetirementDateAttribute(SessionContext ctx, AttributeDescriptor value)
    {
        setProperty(ctx, "retirementDateAttribute", value);
    }


    public void setRetirementDateAttribute(AttributeDescriptor value)
    {
        setRetirementDateAttribute(getSession().getSessionContext(), value);
    }


    public String getRetirementDateExpression(SessionContext ctx)
    {
        return (String)getProperty(ctx, "retirementDateExpression");
    }


    public String getRetirementDateExpression()
    {
        return getRetirementDateExpression(getSession().getSessionContext());
    }


    public void setRetirementDateExpression(SessionContext ctx, String value)
    {
        setProperty(ctx, "retirementDateExpression", value);
    }


    public void setRetirementDateExpression(String value)
    {
        setRetirementDateExpression(getSession().getSessionContext(), value);
    }


    public ComposedType getRetirementItemType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "retirementItemType");
    }


    public ComposedType getRetirementItemType()
    {
        return getRetirementItemType(getSession().getSessionContext());
    }


    public void setRetirementItemType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "retirementItemType", value);
    }


    public void setRetirementItemType(ComposedType value)
    {
        setRetirementItemType(getSession().getSessionContext(), value);
    }
}
