package de.hybris.platform.processing.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.task.Task;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDistributedProcessTransitionTask extends Task
{
    public static final String STATE = "state";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Task.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("state", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public EnumerationValue getState(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "state");
    }


    public EnumerationValue getState()
    {
        return getState(getSession().getSessionContext());
    }


    protected void setState(SessionContext ctx, EnumerationValue value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'state' is not changeable", 0);
        }
        setProperty(ctx, "state", value);
    }


    protected void setState(EnumerationValue value)
    {
        setState(getSession().getSessionContext(), value);
    }
}
