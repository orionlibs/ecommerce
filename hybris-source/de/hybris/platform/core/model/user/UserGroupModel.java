package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.enums.UserTaxGroup;
import de.hybris.platform.platformbackoffice.model.BackofficeSavedQueryModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class UserGroupModel extends PrincipalGroupModel
{
    public static final String _TYPECODE = "UserGroup";
    public static final String _USERGROUPSFORRESTRICTION = "UserGroupsForRestriction";
    public static final String _BACKOFFICESAVEDQUERY2USERGROUPRELATION = "BackofficeSavedQuery2UserGroupRelation";
    public static final String WRITEABLELANGUAGES = "writeableLanguages";
    public static final String READABLELANGUAGES = "readableLanguages";
    public static final String HMCXML = "hmcXML";
    public static final String DENYWRITEPERMISSIONFORALLLANGUAGES = "denyWritePermissionForAllLanguages";
    public static final String USERDISCOUNTGROUP = "userDiscountGroup";
    public static final String USERPRICEGROUP = "userPriceGroup";
    public static final String USERTAXGROUP = "userTaxGroup";
    public static final String RESTRICTIONS = "restrictions";
    public static final String SAVEDQUERIES = "savedQueries";


    public UserGroupModel()
    {
    }


    public UserGroupModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public UserGroupModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public UserGroupModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "denyWritePermissionForAllLanguages", type = Accessor.Type.GETTER)
    public Boolean getDenyWritePermissionForAllLanguages()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("denyWritePermissionForAllLanguages");
    }


    @Accessor(qualifier = "hmcXML", type = Accessor.Type.GETTER)
    public String getHmcXML()
    {
        return (String)getPersistenceContext().getPropertyValue("hmcXML");
    }


    @Accessor(qualifier = "readableLanguages", type = Accessor.Type.GETTER)
    public Collection<LanguageModel> getReadableLanguages()
    {
        return (Collection<LanguageModel>)getPersistenceContext().getPropertyValue("readableLanguages");
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
    public Collection<CMSUserGroupRestrictionModel> getRestrictions()
    {
        return (Collection<CMSUserGroupRestrictionModel>)getPersistenceContext().getPropertyValue("restrictions");
    }


    @Accessor(qualifier = "savedQueries", type = Accessor.Type.GETTER)
    public Collection<BackofficeSavedQueryModel> getSavedQueries()
    {
        return (Collection<BackofficeSavedQueryModel>)getPersistenceContext().getPropertyValue("savedQueries");
    }


    @Accessor(qualifier = "userDiscountGroup", type = Accessor.Type.GETTER)
    public UserDiscountGroup getUserDiscountGroup()
    {
        return (UserDiscountGroup)getPersistenceContext().getPropertyValue("userDiscountGroup");
    }


    @Accessor(qualifier = "userPriceGroup", type = Accessor.Type.GETTER)
    public UserPriceGroup getUserPriceGroup()
    {
        return (UserPriceGroup)getPersistenceContext().getPropertyValue("userPriceGroup");
    }


    @Accessor(qualifier = "userTaxGroup", type = Accessor.Type.GETTER)
    public UserTaxGroup getUserTaxGroup()
    {
        return (UserTaxGroup)getPersistenceContext().getPropertyValue("userTaxGroup");
    }


    @Accessor(qualifier = "writeableLanguages", type = Accessor.Type.GETTER)
    public Collection<LanguageModel> getWriteableLanguages()
    {
        return (Collection<LanguageModel>)getPersistenceContext().getPropertyValue("writeableLanguages");
    }


    @Accessor(qualifier = "denyWritePermissionForAllLanguages", type = Accessor.Type.SETTER)
    public void setDenyWritePermissionForAllLanguages(Boolean value)
    {
        getPersistenceContext().setPropertyValue("denyWritePermissionForAllLanguages", value);
    }


    @Accessor(qualifier = "hmcXML", type = Accessor.Type.SETTER)
    public void setHmcXML(String value)
    {
        getPersistenceContext().setPropertyValue("hmcXML", value);
    }


    @Accessor(qualifier = "readableLanguages", type = Accessor.Type.SETTER)
    public void setReadableLanguages(Collection<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("readableLanguages", value);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
    public void setRestrictions(Collection<CMSUserGroupRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictions", value);
    }


    @Accessor(qualifier = "savedQueries", type = Accessor.Type.SETTER)
    public void setSavedQueries(Collection<BackofficeSavedQueryModel> value)
    {
        getPersistenceContext().setPropertyValue("savedQueries", value);
    }


    @Accessor(qualifier = "userDiscountGroup", type = Accessor.Type.SETTER)
    public void setUserDiscountGroup(UserDiscountGroup value)
    {
        getPersistenceContext().setPropertyValue("userDiscountGroup", value);
    }


    @Accessor(qualifier = "userPriceGroup", type = Accessor.Type.SETTER)
    public void setUserPriceGroup(UserPriceGroup value)
    {
        getPersistenceContext().setPropertyValue("userPriceGroup", value);
    }


    @Accessor(qualifier = "userTaxGroup", type = Accessor.Type.SETTER)
    public void setUserTaxGroup(UserTaxGroup value)
    {
        getPersistenceContext().setPropertyValue("userTaxGroup", value);
    }


    @Accessor(qualifier = "writeableLanguages", type = Accessor.Type.SETTER)
    public void setWriteableLanguages(Collection<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("writeableLanguages", value);
    }
}
