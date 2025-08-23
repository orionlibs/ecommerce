package de.hybris.platform.validation.jalo.constraints;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedRegExpConstraint extends AttributeConstraint
{
    public static final String NOTEMPTY = "notEmpty";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AttributeConstraint.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("notEmpty", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isNotEmpty(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "notEmpty");
    }


    public Boolean isNotEmpty()
    {
        return isNotEmpty(getSession().getSessionContext());
    }


    public boolean isNotEmptyAsPrimitive(SessionContext ctx)
    {
        Boolean value = isNotEmpty(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isNotEmptyAsPrimitive()
    {
        return isNotEmptyAsPrimitive(getSession().getSessionContext());
    }


    public void setNotEmpty(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "notEmpty", value);
    }


    public void setNotEmpty(Boolean value)
    {
        setNotEmpty(getSession().getSessionContext(), value);
    }


    public void setNotEmpty(SessionContext ctx, boolean value)
    {
        setNotEmpty(ctx, Boolean.valueOf(value));
    }


    public void setNotEmpty(boolean value)
    {
        setNotEmpty(getSession().getSessionContext(), value);
    }
}
