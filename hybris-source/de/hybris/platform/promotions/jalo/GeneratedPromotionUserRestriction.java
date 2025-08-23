package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionUserRestriction extends AbstractPromotionRestriction
{
    public static final String POSITIVE = "positive";
    public static final String USERS = "users";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractPromotionRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("positive", Item.AttributeMode.INITIAL);
        tmp.put("users", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isPositive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "positive");
    }


    public Boolean isPositive()
    {
        return isPositive(getSession().getSessionContext());
    }


    public boolean isPositiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isPositive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPositiveAsPrimitive()
    {
        return isPositiveAsPrimitive(getSession().getSessionContext());
    }


    public void setPositive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "positive", value);
    }


    public void setPositive(Boolean value)
    {
        setPositive(getSession().getSessionContext(), value);
    }


    public void setPositive(SessionContext ctx, boolean value)
    {
        setPositive(ctx, Boolean.valueOf(value));
    }


    public void setPositive(boolean value)
    {
        setPositive(getSession().getSessionContext(), value);
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
