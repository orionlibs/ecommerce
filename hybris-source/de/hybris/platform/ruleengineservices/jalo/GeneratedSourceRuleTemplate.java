package de.hybris.platform.ruleengineservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSourceRuleTemplate extends AbstractRuleTemplate
{
    public static final String CONDITIONS = "conditions";
    public static final String ACTIONS = "actions";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRuleTemplate.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("conditions", Item.AttributeMode.INITIAL);
        tmp.put("actions", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getActions(SessionContext ctx)
    {
        return (String)getProperty(ctx, "actions");
    }


    public String getActions()
    {
        return getActions(getSession().getSessionContext());
    }


    public void setActions(SessionContext ctx, String value)
    {
        setProperty(ctx, "actions", value);
    }


    public void setActions(String value)
    {
        setActions(getSession().getSessionContext(), value);
    }


    public String getConditions(SessionContext ctx)
    {
        return (String)getProperty(ctx, "conditions");
    }


    public String getConditions()
    {
        return getConditions(getSession().getSessionContext());
    }


    public void setConditions(SessionContext ctx, String value)
    {
        setProperty(ctx, "conditions", value);
    }


    public void setConditions(String value)
    {
        setConditions(getSession().getSessionContext(), value);
    }
}
