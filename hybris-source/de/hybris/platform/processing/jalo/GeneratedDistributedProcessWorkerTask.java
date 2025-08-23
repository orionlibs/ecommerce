package de.hybris.platform.processing.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.task.Task;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDistributedProcessWorkerTask extends Task
{
    public static final String CONDITIONID = "conditionId";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Task.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("conditionId", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getConditionId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "conditionId");
    }


    public String getConditionId()
    {
        return getConditionId(getSession().getSessionContext());
    }


    protected void setConditionId(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'conditionId' is not changeable", 0);
        }
        setProperty(ctx, "conditionId", value);
    }


    protected void setConditionId(String value)
    {
        setConditionId(getSession().getSessionContext(), value);
    }
}
