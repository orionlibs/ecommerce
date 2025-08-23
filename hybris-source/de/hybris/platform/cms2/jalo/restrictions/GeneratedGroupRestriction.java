package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedGroupRestriction extends AbstractRestriction
{
    public static final String INCLUDESUBGROUPS = "includeSubgroups";
    public static final String USERGROUPS = "userGroups";
    protected static String USERGROUPSFORRESTRICTION_SRC_ORDERED = "relation.UserGroupsForRestriction.source.ordered";
    protected static String USERGROUPSFORRESTRICTION_TGT_ORDERED = "relation.UserGroupsForRestriction.target.ordered";
    protected static String USERGROUPSFORRESTRICTION_MARKMODIFIED = "relation.UserGroupsForRestriction.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("includeSubgroups", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isIncludeSubgroups(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "includeSubgroups");
    }


    public Boolean isIncludeSubgroups()
    {
        return isIncludeSubgroups(getSession().getSessionContext());
    }


    public boolean isIncludeSubgroupsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIncludeSubgroups(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIncludeSubgroupsAsPrimitive()
    {
        return isIncludeSubgroupsAsPrimitive(getSession().getSessionContext());
    }


    public void setIncludeSubgroups(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "includeSubgroups", value);
    }


    public void setIncludeSubgroups(Boolean value)
    {
        setIncludeSubgroups(getSession().getSessionContext(), value);
    }


    public void setIncludeSubgroups(SessionContext ctx, boolean value)
    {
        setIncludeSubgroups(ctx, Boolean.valueOf(value));
    }


    public void setIncludeSubgroups(boolean value)
    {
        setIncludeSubgroups(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("UserGroup");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(USERGROUPSFORRESTRICTION_MARKMODIFIED);
        }
        return true;
    }


    public Collection<UserGroup> getUserGroups(SessionContext ctx)
    {
        List<UserGroup> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.USERGROUPSFORRESTRICTION, "UserGroup", null, false, false);
        return items;
    }


    public Collection<UserGroup> getUserGroups()
    {
        return getUserGroups(getSession().getSessionContext());
    }


    public long getUserGroupsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.USERGROUPSFORRESTRICTION, "UserGroup", null);
    }


    public long getUserGroupsCount()
    {
        return getUserGroupsCount(getSession().getSessionContext());
    }


    public void setUserGroups(SessionContext ctx, Collection<UserGroup> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.USERGROUPSFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(USERGROUPSFORRESTRICTION_MARKMODIFIED));
    }


    public void setUserGroups(Collection<UserGroup> value)
    {
        setUserGroups(getSession().getSessionContext(), value);
    }


    public void addToUserGroups(SessionContext ctx, UserGroup value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.USERGROUPSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(USERGROUPSFORRESTRICTION_MARKMODIFIED));
    }


    public void addToUserGroups(UserGroup value)
    {
        addToUserGroups(getSession().getSessionContext(), value);
    }


    public void removeFromUserGroups(SessionContext ctx, UserGroup value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.USERGROUPSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(USERGROUPSFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromUserGroups(UserGroup value)
    {
        removeFromUserGroups(getSession().getSessionContext(), value);
    }
}
