package de.hybris.platform.platformbackoffice.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.platformbackoffice.constants.GeneratedPlatformbackofficeConstants;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedBackofficeSavedQuery extends GenericItem
{
    public static final String NAME = "name";
    public static final String QUERYOWNER = "queryOwner";
    public static final String TYPECODE = "typeCode";
    public static final String INCLUDESUBTYPES = "includeSubtypes";
    public static final String GLOBALOPERATORCODE = "globalOperatorCode";
    public static final String SORTATTRIBUTE = "sortAttribute";
    public static final String SORTASC = "sortAsc";
    public static final String TOKENIZABLE = "tokenizable";
    public static final String SEARCHMODE = "searchMode";
    public static final String SAVEDQUERIESPARAMETERS = "savedQueriesParameters";
    public static final String CONDITIONS = "conditions";
    public static final String USERGROUPS = "userGroups";
    protected static String BACKOFFICESAVEDQUERY2USERGROUPRELATION_SRC_ORDERED = "relation.BackofficeSavedQuery2UserGroupRelation.source.ordered";
    protected static String BACKOFFICESAVEDQUERY2USERGROUPRELATION_TGT_ORDERED = "relation.BackofficeSavedQuery2UserGroupRelation.target.ordered";
    protected static String BACKOFFICESAVEDQUERY2USERGROUPRELATION_MARKMODIFIED = "relation.BackofficeSavedQuery2UserGroupRelation.markmodified";
    protected static final OneToManyHandler<BackofficeSearchCondition> CONDITIONSHANDLER = new OneToManyHandler(GeneratedPlatformbackofficeConstants.TC.BACKOFFICESEARCHCONDITION, true, "savedQuery", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("queryOwner", Item.AttributeMode.INITIAL);
        tmp.put("typeCode", Item.AttributeMode.INITIAL);
        tmp.put("includeSubtypes", Item.AttributeMode.INITIAL);
        tmp.put("globalOperatorCode", Item.AttributeMode.INITIAL);
        tmp.put("sortAttribute", Item.AttributeMode.INITIAL);
        tmp.put("sortAsc", Item.AttributeMode.INITIAL);
        tmp.put("tokenizable", Item.AttributeMode.INITIAL);
        tmp.put("searchMode", Item.AttributeMode.INITIAL);
        tmp.put("savedQueriesParameters", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<BackofficeSearchCondition> getConditions(SessionContext ctx)
    {
        return CONDITIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<BackofficeSearchCondition> getConditions()
    {
        return getConditions(getSession().getSessionContext());
    }


    public void setConditions(SessionContext ctx, Collection<BackofficeSearchCondition> value)
    {
        CONDITIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setConditions(Collection<BackofficeSearchCondition> value)
    {
        setConditions(getSession().getSessionContext(), value);
    }


    public void addToConditions(SessionContext ctx, BackofficeSearchCondition value)
    {
        CONDITIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToConditions(BackofficeSearchCondition value)
    {
        addToConditions(getSession().getSessionContext(), value);
    }


    public void removeFromConditions(SessionContext ctx, BackofficeSearchCondition value)
    {
        CONDITIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromConditions(BackofficeSearchCondition value)
    {
        removeFromConditions(getSession().getSessionContext(), value);
    }


    public String getGlobalOperatorCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "globalOperatorCode");
    }


    public String getGlobalOperatorCode()
    {
        return getGlobalOperatorCode(getSession().getSessionContext());
    }


    public void setGlobalOperatorCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "globalOperatorCode", value);
    }


    public void setGlobalOperatorCode(String value)
    {
        setGlobalOperatorCode(getSession().getSessionContext(), value);
    }


    public Boolean isIncludeSubtypes(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "includeSubtypes");
    }


    public Boolean isIncludeSubtypes()
    {
        return isIncludeSubtypes(getSession().getSessionContext());
    }


    public boolean isIncludeSubtypesAsPrimitive(SessionContext ctx)
    {
        Boolean value = isIncludeSubtypes(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isIncludeSubtypesAsPrimitive()
    {
        return isIncludeSubtypesAsPrimitive(getSession().getSessionContext());
    }


    public void setIncludeSubtypes(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "includeSubtypes", value);
    }


    public void setIncludeSubtypes(Boolean value)
    {
        setIncludeSubtypes(getSession().getSessionContext(), value);
    }


    public void setIncludeSubtypes(SessionContext ctx, boolean value)
    {
        setIncludeSubtypes(ctx, Boolean.valueOf(value));
    }


    public void setIncludeSubtypes(boolean value)
    {
        setIncludeSubtypes(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("UserGroup");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(BACKOFFICESAVEDQUERY2USERGROUPRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedBackofficeSavedQuery.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedBackofficeSavedQuery.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public User getQueryOwner(SessionContext ctx)
    {
        return (User)getProperty(ctx, "queryOwner");
    }


    public User getQueryOwner()
    {
        return getQueryOwner(getSession().getSessionContext());
    }


    public void setQueryOwner(SessionContext ctx, User value)
    {
        setProperty(ctx, "queryOwner", value);
    }


    public void setQueryOwner(User value)
    {
        setQueryOwner(getSession().getSessionContext(), value);
    }


    public Map<String, String> getAllSavedQueriesParameters(SessionContext ctx)
    {
        Map<String, String> map = (Map<String, String>)getProperty(ctx, "savedQueriesParameters");
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllSavedQueriesParameters()
    {
        return getAllSavedQueriesParameters(getSession().getSessionContext());
    }


    public void setAllSavedQueriesParameters(SessionContext ctx, Map<String, String> value)
    {
        setProperty(ctx, "savedQueriesParameters", value);
    }


    public void setAllSavedQueriesParameters(Map<String, String> value)
    {
        setAllSavedQueriesParameters(getSession().getSessionContext(), value);
    }


    public String getSearchMode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "searchMode");
    }


    public String getSearchMode()
    {
        return getSearchMode(getSession().getSessionContext());
    }


    public void setSearchMode(SessionContext ctx, String value)
    {
        setProperty(ctx, "searchMode", value);
    }


    public void setSearchMode(String value)
    {
        setSearchMode(getSession().getSessionContext(), value);
    }


    public Boolean isSortAsc(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "sortAsc");
    }


    public Boolean isSortAsc()
    {
        return isSortAsc(getSession().getSessionContext());
    }


    public boolean isSortAscAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSortAsc(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSortAscAsPrimitive()
    {
        return isSortAscAsPrimitive(getSession().getSessionContext());
    }


    public void setSortAsc(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "sortAsc", value);
    }


    public void setSortAsc(Boolean value)
    {
        setSortAsc(getSession().getSessionContext(), value);
    }


    public void setSortAsc(SessionContext ctx, boolean value)
    {
        setSortAsc(ctx, Boolean.valueOf(value));
    }


    public void setSortAsc(boolean value)
    {
        setSortAsc(getSession().getSessionContext(), value);
    }


    public String getSortAttribute(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sortAttribute");
    }


    public String getSortAttribute()
    {
        return getSortAttribute(getSession().getSessionContext());
    }


    public void setSortAttribute(SessionContext ctx, String value)
    {
        setProperty(ctx, "sortAttribute", value);
    }


    public void setSortAttribute(String value)
    {
        setSortAttribute(getSession().getSessionContext(), value);
    }


    public Boolean isTokenizable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "tokenizable");
    }


    public Boolean isTokenizable()
    {
        return isTokenizable(getSession().getSessionContext());
    }


    public boolean isTokenizableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isTokenizable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isTokenizableAsPrimitive()
    {
        return isTokenizableAsPrimitive(getSession().getSessionContext());
    }


    public void setTokenizable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "tokenizable", value);
    }


    public void setTokenizable(Boolean value)
    {
        setTokenizable(getSession().getSessionContext(), value);
    }


    public void setTokenizable(SessionContext ctx, boolean value)
    {
        setTokenizable(ctx, Boolean.valueOf(value));
    }


    public void setTokenizable(boolean value)
    {
        setTokenizable(getSession().getSessionContext(), value);
    }


    public String getTypeCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "typeCode");
    }


    public String getTypeCode()
    {
        return getTypeCode(getSession().getSessionContext());
    }


    public void setTypeCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "typeCode", value);
    }


    public void setTypeCode(String value)
    {
        setTypeCode(getSession().getSessionContext(), value);
    }


    public Collection<UserGroup> getUserGroups(SessionContext ctx)
    {
        List<UserGroup> items = getLinkedItems(ctx, true, GeneratedPlatformbackofficeConstants.Relations.BACKOFFICESAVEDQUERY2USERGROUPRELATION, "UserGroup", null, false, false);
        return items;
    }


    public Collection<UserGroup> getUserGroups()
    {
        return getUserGroups(getSession().getSessionContext());
    }


    public long getUserGroupsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedPlatformbackofficeConstants.Relations.BACKOFFICESAVEDQUERY2USERGROUPRELATION, "UserGroup", null);
    }


    public long getUserGroupsCount()
    {
        return getUserGroupsCount(getSession().getSessionContext());
    }


    public void setUserGroups(SessionContext ctx, Collection<UserGroup> value)
    {
        setLinkedItems(ctx, true, GeneratedPlatformbackofficeConstants.Relations.BACKOFFICESAVEDQUERY2USERGROUPRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(BACKOFFICESAVEDQUERY2USERGROUPRELATION_MARKMODIFIED));
    }


    public void setUserGroups(Collection<UserGroup> value)
    {
        setUserGroups(getSession().getSessionContext(), value);
    }


    public void addToUserGroups(SessionContext ctx, UserGroup value)
    {
        addLinkedItems(ctx, true, GeneratedPlatformbackofficeConstants.Relations.BACKOFFICESAVEDQUERY2USERGROUPRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(BACKOFFICESAVEDQUERY2USERGROUPRELATION_MARKMODIFIED));
    }


    public void addToUserGroups(UserGroup value)
    {
        addToUserGroups(getSession().getSessionContext(), value);
    }


    public void removeFromUserGroups(SessionContext ctx, UserGroup value)
    {
        removeLinkedItems(ctx, true, GeneratedPlatformbackofficeConstants.Relations.BACKOFFICESAVEDQUERY2USERGROUPRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(BACKOFFICESAVEDQUERY2USERGROUPRELATION_MARKMODIFIED));
    }


    public void removeFromUserGroups(UserGroup value)
    {
        removeFromUserGroups(getSession().getSessionContext(), value);
    }
}
