package com.hybris.backoffice.jalo.user;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.UserGroup;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBackofficeRole extends UserGroup
{
    public static final String AUTHORITIES = "authorities";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(UserGroup.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("authorities", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<String> getAuthorities(SessionContext ctx)
    {
        Collection<String> coll = (Collection<String>)getProperty(ctx, "authorities");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<String> getAuthorities()
    {
        return getAuthorities(getSession().getSessionContext());
    }


    public void setAuthorities(SessionContext ctx, Collection<String> value)
    {
        setProperty(ctx, "authorities", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setAuthorities(Collection<String> value)
    {
        setAuthorities(getSession().getSessionContext(), value);
    }
}
