package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class CMSUserGroupRestrictionModel extends AbstractRestrictionModel
{
    public static final String _TYPECODE = "CMSUserGroupRestriction";
    public static final String INCLUDESUBGROUPS = "includeSubgroups";
    public static final String USERGROUPS = "userGroups";


    public CMSUserGroupRestrictionModel()
    {
    }


    public CMSUserGroupRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSUserGroupRestrictionModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSUserGroupRestrictionModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "userGroups", type = Accessor.Type.GETTER)
    public Collection<UserGroupModel> getUserGroups()
    {
        return (Collection<UserGroupModel>)getPersistenceContext().getPropertyValue("userGroups");
    }


    @Accessor(qualifier = "includeSubgroups", type = Accessor.Type.GETTER)
    public boolean isIncludeSubgroups()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("includeSubgroups"));
    }


    @Accessor(qualifier = "includeSubgroups", type = Accessor.Type.SETTER)
    public void setIncludeSubgroups(boolean value)
    {
        getPersistenceContext().setPropertyValue("includeSubgroups", toObject(value));
    }


    @Accessor(qualifier = "userGroups", type = Accessor.Type.SETTER)
    public void setUserGroups(Collection<UserGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("userGroups", value);
    }
}
