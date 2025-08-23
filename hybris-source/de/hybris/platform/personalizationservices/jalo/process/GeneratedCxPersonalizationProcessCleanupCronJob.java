package de.hybris.platform.personalizationservices.jalo.process;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCxPersonalizationProcessCleanupCronJob extends CronJob
{
    public static final String PROCESSSTATES = "processStates";
    public static final String MAXPROCESSAGE = "maxProcessAge";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("processStates", Item.AttributeMode.INITIAL);
        tmp.put("maxProcessAge", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getMaxProcessAge(SessionContext ctx)
    {
        return (String)getProperty(ctx, "maxProcessAge");
    }


    public String getMaxProcessAge()
    {
        return getMaxProcessAge(getSession().getSessionContext());
    }


    public void setMaxProcessAge(SessionContext ctx, String value)
    {
        setProperty(ctx, "maxProcessAge", value);
    }


    public void setMaxProcessAge(String value)
    {
        setMaxProcessAge(getSession().getSessionContext(), value);
    }


    public Collection<EnumerationValue> getProcessStates(SessionContext ctx)
    {
        Collection<EnumerationValue> coll = (Collection<EnumerationValue>)getProperty(ctx, "processStates");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<EnumerationValue> getProcessStates()
    {
        return getProcessStates(getSession().getSessionContext());
    }


    public void setProcessStates(SessionContext ctx, Collection<EnumerationValue> value)
    {
        setProperty(ctx, "processStates", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setProcessStates(Collection<EnumerationValue> value)
    {
        setProcessStates(getSession().getSessionContext(), value);
    }
}
