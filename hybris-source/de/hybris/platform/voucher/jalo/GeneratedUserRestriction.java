package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedUserRestriction extends Restriction
{
    public static final String USERS = "users";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Restriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("users", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Principal> getUsers(SessionContext ctx)
    {
        Collection<Principal> coll = (Collection<Principal>)getProperty(ctx, "users");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Principal> getUsers()
    {
        return getUsers(getSession().getSessionContext());
    }


    public void setUsers(SessionContext ctx, Collection<Principal> value)
    {
        setProperty(ctx, "users", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setUsers(Collection<Principal> value)
    {
        setUsers(getSession().getSessionContext(), value);
    }
}
