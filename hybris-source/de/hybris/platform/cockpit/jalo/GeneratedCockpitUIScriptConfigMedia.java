package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCockpitUIScriptConfigMedia extends CockpitUIConfigurationMedia
{
    public static final String ALLOWSCRIPTEVALUATION = "allowScriptEvaluation";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CockpitUIConfigurationMedia.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("allowScriptEvaluation", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAllowScriptEvaluation(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "allowScriptEvaluation");
    }


    public Boolean isAllowScriptEvaluation()
    {
        return isAllowScriptEvaluation(getSession().getSessionContext());
    }


    public boolean isAllowScriptEvaluationAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAllowScriptEvaluation(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAllowScriptEvaluationAsPrimitive()
    {
        return isAllowScriptEvaluationAsPrimitive(getSession().getSessionContext());
    }


    public void setAllowScriptEvaluation(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "allowScriptEvaluation", value);
    }


    public void setAllowScriptEvaluation(Boolean value)
    {
        setAllowScriptEvaluation(getSession().getSessionContext(), value);
    }


    public void setAllowScriptEvaluation(SessionContext ctx, boolean value)
    {
        setAllowScriptEvaluation(ctx, Boolean.valueOf(value));
    }


    public void setAllowScriptEvaluation(boolean value)
    {
        setAllowScriptEvaluation(getSession().getSessionContext(), value);
    }
}
