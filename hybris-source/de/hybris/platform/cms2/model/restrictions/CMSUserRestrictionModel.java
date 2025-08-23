package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class CMSUserRestrictionModel extends AbstractRestrictionModel
{
    public static final String _TYPECODE = "CMSUserRestriction";
    public static final String USERS = "users";


    public CMSUserRestrictionModel()
    {
    }


    public CMSUserRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSUserRestrictionModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSUserRestrictionModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "users", type = Accessor.Type.GETTER)
    public Collection<UserModel> getUsers()
    {
        return (Collection<UserModel>)getPersistenceContext().getPropertyValue("users");
    }


    @Accessor(qualifier = "users", type = Accessor.Type.SETTER)
    public void setUsers(Collection<UserModel> value)
    {
        getPersistenceContext().setPropertyValue("users", value);
    }
}
