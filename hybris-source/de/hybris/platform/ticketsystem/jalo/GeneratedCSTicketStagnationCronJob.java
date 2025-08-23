package de.hybris.platform.ticketsystem.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Deprecated(since = "6.6", forRemoval = true)
public abstract class GeneratedCSTicketStagnationCronJob extends CronJob
{
    public static final String STAGNATIONPERIOD = "stagnationPeriod";
    public static final String ELIGIBLESTATES = "eligibleStates";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("stagnationPeriod", Item.AttributeMode.INITIAL);
        tmp.put("eligibleStates", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getEligibleStates(SessionContext ctx)
    {
        return (String)getProperty(ctx, "eligibleStates");
    }


    public String getEligibleStates()
    {
        return getEligibleStates(getSession().getSessionContext());
    }


    public void setEligibleStates(SessionContext ctx, String value)
    {
        setProperty(ctx, "eligibleStates", value);
    }


    public void setEligibleStates(String value)
    {
        setEligibleStates(getSession().getSessionContext(), value);
    }


    public Integer getStagnationPeriod(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "stagnationPeriod");
    }


    public Integer getStagnationPeriod()
    {
        return getStagnationPeriod(getSession().getSessionContext());
    }


    public int getStagnationPeriodAsPrimitive(SessionContext ctx)
    {
        Integer value = getStagnationPeriod(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getStagnationPeriodAsPrimitive()
    {
        return getStagnationPeriodAsPrimitive(getSession().getSessionContext());
    }


    public void setStagnationPeriod(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "stagnationPeriod", value);
    }


    public void setStagnationPeriod(Integer value)
    {
        setStagnationPeriod(getSession().getSessionContext(), value);
    }


    public void setStagnationPeriod(SessionContext ctx, int value)
    {
        setStagnationPeriod(ctx, Integer.valueOf(value));
    }


    public void setStagnationPeriod(int value)
    {
        setStagnationPeriod(getSession().getSessionContext(), value);
    }
}
