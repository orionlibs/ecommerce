package de.hybris.platform.admincockpit.jalo.cronjob;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRemoveOrphanedFilesCronJob extends CronJob
{
    public static final String PAGING = "paging";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("paging", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getPaging(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "paging");
    }


    public Integer getPaging()
    {
        return getPaging(getSession().getSessionContext());
    }


    public int getPagingAsPrimitive(SessionContext ctx)
    {
        Integer value = getPaging(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getPagingAsPrimitive()
    {
        return getPagingAsPrimitive(getSession().getSessionContext());
    }


    public void setPaging(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "paging", value);
    }


    public void setPaging(Integer value)
    {
        setPaging(getSession().getSessionContext(), value);
    }


    public void setPaging(SessionContext ctx, int value)
    {
        setPaging(ctx, Integer.valueOf(value));
    }


    public void setPaging(int value)
    {
        setPaging(getSession().getSessionContext(), value);
    }
}
