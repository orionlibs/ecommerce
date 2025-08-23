package de.hybris.platform.platformbackoffice.validation.jalo.constraints;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.validation.jalo.constraints.AttributeConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedHybrisEnumValueCodeConstraint extends AttributeConstraint
{
    public static final String VALUE = "value";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AttributeConstraint.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("value", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getValue(SessionContext ctx)
    {
        return (String)getProperty(ctx, "value");
    }


    public String getValue()
    {
        return getValue(getSession().getSessionContext());
    }


    public void setValue(SessionContext ctx, String value)
    {
        setProperty(ctx, "value", value);
    }


    public void setValue(String value)
    {
        setValue(getSession().getSessionContext(), value);
    }
}
