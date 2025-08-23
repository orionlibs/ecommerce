package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxResultsCleaningCronJob extends CronJob
{
    public static final String MAXRESULTSAGE = "maxResultsAge";
    public static final String ANONYMOUS = "anonymous";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("maxResultsAge", Item.AttributeMode.INITIAL);
        tmp.put("anonymous", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAnonymous(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "anonymous");
    }


    public Boolean isAnonymous()
    {
        return isAnonymous(getSession().getSessionContext());
    }


    public boolean isAnonymousAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAnonymous(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAnonymousAsPrimitive()
    {
        return isAnonymousAsPrimitive(getSession().getSessionContext());
    }


    public void setAnonymous(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "anonymous", value);
    }


    public void setAnonymous(Boolean value)
    {
        setAnonymous(getSession().getSessionContext(), value);
    }


    public void setAnonymous(SessionContext ctx, boolean value)
    {
        setAnonymous(ctx, Boolean.valueOf(value));
    }


    public void setAnonymous(boolean value)
    {
        setAnonymous(getSession().getSessionContext(), value);
    }


    public Integer getMaxResultsAge(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxResultsAge");
    }


    public Integer getMaxResultsAge()
    {
        return getMaxResultsAge(getSession().getSessionContext());
    }


    public int getMaxResultsAgeAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxResultsAge(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxResultsAgeAsPrimitive()
    {
        return getMaxResultsAgeAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxResultsAge(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxResultsAge", value);
    }


    public void setMaxResultsAge(Integer value)
    {
        setMaxResultsAge(getSession().getSessionContext(), value);
    }


    public void setMaxResultsAge(SessionContext ctx, int value)
    {
        setMaxResultsAge(ctx, Integer.valueOf(value));
    }


    public void setMaxResultsAge(int value)
    {
        setMaxResultsAge(getSession().getSessionContext(), value);
    }
}
