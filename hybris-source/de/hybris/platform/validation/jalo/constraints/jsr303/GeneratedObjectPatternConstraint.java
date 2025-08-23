package de.hybris.platform.validation.jalo.constraints.jsr303;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.validation.jalo.constraints.AttributeConstraint;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedObjectPatternConstraint extends AttributeConstraint
{
    public static final String REGEXP = "regexp";
    public static final String FLAGS = "flags";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AttributeConstraint.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("regexp", Item.AttributeMode.INITIAL);
        tmp.put("flags", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Set<EnumerationValue> getFlags(SessionContext ctx)
    {
        Set<EnumerationValue> coll = (Set<EnumerationValue>)getProperty(ctx, "flags");
        return (coll != null) ? coll : Collections.EMPTY_SET;
    }


    public Set<EnumerationValue> getFlags()
    {
        return getFlags(getSession().getSessionContext());
    }


    public void setFlags(SessionContext ctx, Set<EnumerationValue> value)
    {
        setProperty(ctx, "flags", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setFlags(Set<EnumerationValue> value)
    {
        setFlags(getSession().getSessionContext(), value);
    }


    public String getRegexp(SessionContext ctx)
    {
        return (String)getProperty(ctx, "regexp");
    }


    public String getRegexp()
    {
        return getRegexp(getSession().getSessionContext());
    }


    public void setRegexp(SessionContext ctx, String value)
    {
        setProperty(ctx, "regexp", value);
    }


    public void setRegexp(String value)
    {
        setRegexp(getSession().getSessionContext(), value);
    }
}
