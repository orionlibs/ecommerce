package de.hybris.platform.samlsinglesignon.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.samlsinglesignon.constants.GeneratedSamlsinglesignonConstants;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedSamlUserGroup extends GenericItem
{
    public static final String SAMLUSERGROUP = "samlUserGroup";
    public static final String USERTYPE = "userType";
    public static final String USERGROUPS = "userGroups";
    protected static String SAMLUSERGROUPTOUSERGROUPRELATION_SRC_ORDERED = "relation.SamlUserGroupToUserGroupRelation.source.ordered";
    protected static String SAMLUSERGROUPTOUSERGROUPRELATION_TGT_ORDERED = "relation.SamlUserGroupToUserGroupRelation.target.ordered";
    protected static String SAMLUSERGROUPTOUSERGROUPRELATION_MARKMODIFIED = "relation.SamlUserGroupToUserGroupRelation.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("samlUserGroup", Item.AttributeMode.INITIAL);
        tmp.put("userType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("UserGroup");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(SAMLUSERGROUPTOUSERGROUPRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getSamlUserGroup(SessionContext ctx)
    {
        return (String)getProperty(ctx, "samlUserGroup");
    }


    public String getSamlUserGroup()
    {
        return getSamlUserGroup(getSession().getSessionContext());
    }


    public void setSamlUserGroup(SessionContext ctx, String value)
    {
        setProperty(ctx, "samlUserGroup", value);
    }


    public void setSamlUserGroup(String value)
    {
        setSamlUserGroup(getSession().getSessionContext(), value);
    }


    public Set<UserGroup> getUserGroups(SessionContext ctx)
    {
        List<UserGroup> items = getLinkedItems(ctx, true, GeneratedSamlsinglesignonConstants.Relations.SAMLUSERGROUPTOUSERGROUPRELATION, "UserGroup", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<UserGroup> getUserGroups()
    {
        return getUserGroups(getSession().getSessionContext());
    }


    public long getUserGroupsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedSamlsinglesignonConstants.Relations.SAMLUSERGROUPTOUSERGROUPRELATION, "UserGroup", null);
    }


    public long getUserGroupsCount()
    {
        return getUserGroupsCount(getSession().getSessionContext());
    }


    public void setUserGroups(SessionContext ctx, Set<UserGroup> value)
    {
        setLinkedItems(ctx, true, GeneratedSamlsinglesignonConstants.Relations.SAMLUSERGROUPTOUSERGROUPRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(SAMLUSERGROUPTOUSERGROUPRELATION_MARKMODIFIED));
    }


    public void setUserGroups(Set<UserGroup> value)
    {
        setUserGroups(getSession().getSessionContext(), value);
    }


    public void addToUserGroups(SessionContext ctx, UserGroup value)
    {
        addLinkedItems(ctx, true, GeneratedSamlsinglesignonConstants.Relations.SAMLUSERGROUPTOUSERGROUPRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(SAMLUSERGROUPTOUSERGROUPRELATION_MARKMODIFIED));
    }


    public void addToUserGroups(UserGroup value)
    {
        addToUserGroups(getSession().getSessionContext(), value);
    }


    public void removeFromUserGroups(SessionContext ctx, UserGroup value)
    {
        removeLinkedItems(ctx, true, GeneratedSamlsinglesignonConstants.Relations.SAMLUSERGROUPTOUSERGROUPRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(SAMLUSERGROUPTOUSERGROUPRELATION_MARKMODIFIED));
    }


    public void removeFromUserGroups(UserGroup value)
    {
        removeFromUserGroups(getSession().getSessionContext(), value);
    }


    public Type getUserType(SessionContext ctx)
    {
        return (Type)getProperty(ctx, "userType");
    }


    public Type getUserType()
    {
        return getUserType(getSession().getSessionContext());
    }


    public void setUserType(SessionContext ctx, Type value)
    {
        setProperty(ctx, "userType", value);
    }


    public void setUserType(Type value)
    {
        setUserType(getSession().getSessionContext(), value);
    }
}
