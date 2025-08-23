package de.hybris.platform.task.jalo;

import de.hybris.platform.cronjob.jalo.Trigger;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.task.Task;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedTriggerTask extends Task
{
    public static final String TRIGGER = "trigger";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Task.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("trigger", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Trigger getTrigger(SessionContext ctx)
    {
        return (Trigger)getProperty(ctx, "trigger");
    }


    public Trigger getTrigger()
    {
        return getTrigger(getSession().getSessionContext());
    }


    public void setTrigger(SessionContext ctx, Trigger value)
    {
        setProperty(ctx, "trigger", value);
    }


    public void setTrigger(Trigger value)
    {
        setTrigger(getSession().getSessionContext(), value);
    }
}
