package de.hybris.platform.platformbackoffice.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public class BackofficeSavedQueryModel extends ItemModel
{
    public static final String _TYPECODE = "BackofficeSavedQuery";
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


    public BackofficeSavedQueryModel()
    {
    }


    public BackofficeSavedQueryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeSavedQueryModel(UserModel _queryOwner, String _typeCode)
    {
        setQueryOwner(_queryOwner);
        setTypeCode(_typeCode);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeSavedQueryModel(ItemModel _owner, UserModel _queryOwner, String _typeCode)
    {
        setOwner(_owner);
        setQueryOwner(_queryOwner);
        setTypeCode(_typeCode);
    }


    @Accessor(qualifier = "conditions", type = Accessor.Type.GETTER)
    public Collection<BackofficeSearchConditionModel> getConditions()
    {
        return (Collection<BackofficeSearchConditionModel>)getPersistenceContext().getPropertyValue("conditions");
    }


    @Accessor(qualifier = "globalOperatorCode", type = Accessor.Type.GETTER)
    public String getGlobalOperatorCode()
    {
        return (String)getPersistenceContext().getPropertyValue("globalOperatorCode");
    }


    @Accessor(qualifier = "includeSubtypes", type = Accessor.Type.GETTER)
    public Boolean getIncludeSubtypes()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("includeSubtypes");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "queryOwner", type = Accessor.Type.GETTER)
    public UserModel getQueryOwner()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("queryOwner");
    }


    @Accessor(qualifier = "savedQueriesParameters", type = Accessor.Type.GETTER)
    public Map<String, String> getSavedQueriesParameters()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("savedQueriesParameters");
    }


    @Accessor(qualifier = "searchMode", type = Accessor.Type.GETTER)
    public String getSearchMode()
    {
        return (String)getPersistenceContext().getPropertyValue("searchMode");
    }


    @Accessor(qualifier = "sortAsc", type = Accessor.Type.GETTER)
    public Boolean getSortAsc()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("sortAsc");
    }


    @Accessor(qualifier = "sortAttribute", type = Accessor.Type.GETTER)
    public String getSortAttribute()
    {
        return (String)getPersistenceContext().getPropertyValue("sortAttribute");
    }


    @Accessor(qualifier = "tokenizable", type = Accessor.Type.GETTER)
    public Boolean getTokenizable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("tokenizable");
    }


    @Accessor(qualifier = "typeCode", type = Accessor.Type.GETTER)
    public String getTypeCode()
    {
        return (String)getPersistenceContext().getPropertyValue("typeCode");
    }


    @Accessor(qualifier = "userGroups", type = Accessor.Type.GETTER)
    public Collection<UserGroupModel> getUserGroups()
    {
        return (Collection<UserGroupModel>)getPersistenceContext().getPropertyValue("userGroups");
    }


    @Accessor(qualifier = "conditions", type = Accessor.Type.SETTER)
    public void setConditions(Collection<BackofficeSearchConditionModel> value)
    {
        getPersistenceContext().setPropertyValue("conditions", value);
    }


    @Accessor(qualifier = "globalOperatorCode", type = Accessor.Type.SETTER)
    public void setGlobalOperatorCode(String value)
    {
        getPersistenceContext().setPropertyValue("globalOperatorCode", value);
    }


    @Accessor(qualifier = "includeSubtypes", type = Accessor.Type.SETTER)
    public void setIncludeSubtypes(Boolean value)
    {
        getPersistenceContext().setPropertyValue("includeSubtypes", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "queryOwner", type = Accessor.Type.SETTER)
    public void setQueryOwner(UserModel value)
    {
        getPersistenceContext().setPropertyValue("queryOwner", value);
    }


    @Accessor(qualifier = "savedQueriesParameters", type = Accessor.Type.SETTER)
    public void setSavedQueriesParameters(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("savedQueriesParameters", value);
    }


    @Accessor(qualifier = "searchMode", type = Accessor.Type.SETTER)
    public void setSearchMode(String value)
    {
        getPersistenceContext().setPropertyValue("searchMode", value);
    }


    @Accessor(qualifier = "sortAsc", type = Accessor.Type.SETTER)
    public void setSortAsc(Boolean value)
    {
        getPersistenceContext().setPropertyValue("sortAsc", value);
    }


    @Accessor(qualifier = "sortAttribute", type = Accessor.Type.SETTER)
    public void setSortAttribute(String value)
    {
        getPersistenceContext().setPropertyValue("sortAttribute", value);
    }


    @Accessor(qualifier = "tokenizable", type = Accessor.Type.SETTER)
    public void setTokenizable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("tokenizable", value);
    }


    @Accessor(qualifier = "typeCode", type = Accessor.Type.SETTER)
    public void setTypeCode(String value)
    {
        getPersistenceContext().setPropertyValue("typeCode", value);
    }


    @Accessor(qualifier = "userGroups", type = Accessor.Type.SETTER)
    public void setUserGroups(Collection<UserGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("userGroups", value);
    }
}
