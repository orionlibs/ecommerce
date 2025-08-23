package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedMediaProcessCronJob extends CronJob
{
    public static final String JOBMEDIA = "jobMedia";
    public static final String CURRENTLINE = "currentLine";
    public static final String LASTSUCCESSFULLINE = "lastSuccessfulLine";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("jobMedia", Item.AttributeMode.INITIAL);
        tmp.put("currentLine", Item.AttributeMode.INITIAL);
        tmp.put("lastSuccessfulLine", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getCurrentLine(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "currentLine");
    }


    public Integer getCurrentLine()
    {
        return getCurrentLine(getSession().getSessionContext());
    }


    public int getCurrentLineAsPrimitive(SessionContext ctx)
    {
        Integer value = getCurrentLine(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getCurrentLineAsPrimitive()
    {
        return getCurrentLineAsPrimitive(getSession().getSessionContext());
    }


    public void setCurrentLine(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "currentLine", value);
    }


    public void setCurrentLine(Integer value)
    {
        setCurrentLine(getSession().getSessionContext(), value);
    }


    public void setCurrentLine(SessionContext ctx, int value)
    {
        setCurrentLine(ctx, Integer.valueOf(value));
    }


    public void setCurrentLine(int value)
    {
        setCurrentLine(getSession().getSessionContext(), value);
    }


    public JobMedia getJobMedia(SessionContext ctx)
    {
        return (JobMedia)getProperty(ctx, "jobMedia");
    }


    public JobMedia getJobMedia()
    {
        return getJobMedia(getSession().getSessionContext());
    }


    public void setJobMedia(SessionContext ctx, JobMedia value)
    {
        setProperty(ctx, "jobMedia", value);
    }


    public void setJobMedia(JobMedia value)
    {
        setJobMedia(getSession().getSessionContext(), value);
    }


    public Integer getLastSuccessfulLine(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "lastSuccessfulLine");
    }


    public Integer getLastSuccessfulLine()
    {
        return getLastSuccessfulLine(getSession().getSessionContext());
    }


    public int getLastSuccessfulLineAsPrimitive(SessionContext ctx)
    {
        Integer value = getLastSuccessfulLine(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getLastSuccessfulLineAsPrimitive()
    {
        return getLastSuccessfulLineAsPrimitive(getSession().getSessionContext());
    }


    public void setLastSuccessfulLine(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "lastSuccessfulLine", value);
    }


    public void setLastSuccessfulLine(Integer value)
    {
        setLastSuccessfulLine(getSession().getSessionContext(), value);
    }


    public void setLastSuccessfulLine(SessionContext ctx, int value)
    {
        setLastSuccessfulLine(ctx, Integer.valueOf(value));
    }


    public void setLastSuccessfulLine(int value)
    {
        setLastSuccessfulLine(getSession().getSessionContext(), value);
    }
}
