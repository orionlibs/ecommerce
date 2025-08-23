package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedUserRestriction extends AbstractRestriction
{
    public static final String USERS = "users";
    protected static String USERSFORRESTRICTION_SRC_ORDERED = "relation.UsersForRestriction.source.ordered";
    protected static String USERSFORRESTRICTION_TGT_ORDERED = "relation.UsersForRestriction.target.ordered";
    protected static String USERSFORRESTRICTION_MARKMODIFIED = "relation.UsersForRestriction.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("User");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(USERSFORRESTRICTION_MARKMODIFIED);
        }
        return true;
    }


    public Collection<User> getUsers(SessionContext ctx)
    {
        List<User> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.USERSFORRESTRICTION, "User", null, false, false);
        return items;
    }


    public Collection<User> getUsers()
    {
        return getUsers(getSession().getSessionContext());
    }


    public long getUsersCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.USERSFORRESTRICTION, "User", null);
    }


    public long getUsersCount()
    {
        return getUsersCount(getSession().getSessionContext());
    }


    public void setUsers(SessionContext ctx, Collection<User> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.USERSFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(USERSFORRESTRICTION_MARKMODIFIED));
    }


    public void setUsers(Collection<User> value)
    {
        setUsers(getSession().getSessionContext(), value);
    }


    public void addToUsers(SessionContext ctx, User value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.USERSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(USERSFORRESTRICTION_MARKMODIFIED));
    }


    public void addToUsers(User value)
    {
        addToUsers(getSession().getSessionContext(), value);
    }


    public void removeFromUsers(SessionContext ctx, User value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.USERSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(USERSFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromUsers(User value)
    {
        removeFromUsers(getSession().getSessionContext(), value);
    }
}
