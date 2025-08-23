package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class B2BUserGroupModel extends UserGroupModel
{
    public static final String _TYPECODE = "B2BUserGroup";
    public static final String _B2BUNIT2B2BUSERGROUPS = "B2BUnit2B2BUserGroups";
    public static final String UNIT = "Unit";
    public static final String PERMISSIONS = "Permissions";


    public B2BUserGroupModel()
    {
    }


    public B2BUserGroupModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BUserGroupModel(B2BUnitModel _Unit, String _uid)
    {
        setUnit(_Unit);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BUserGroupModel(B2BUnitModel _Unit, ItemModel _owner, String _uid)
    {
        setUnit(_Unit);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "Permissions", type = Accessor.Type.GETTER)
    public List<B2BPermissionModel> getPermissions()
    {
        return (List<B2BPermissionModel>)getPersistenceContext().getPropertyValue("Permissions");
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.GETTER)
    public B2BUnitModel getUnit()
    {
        return (B2BUnitModel)getPersistenceContext().getPropertyValue("Unit");
    }


    @Accessor(qualifier = "Permissions", type = Accessor.Type.SETTER)
    public void setPermissions(List<B2BPermissionModel> value)
    {
        getPersistenceContext().setPropertyValue("Permissions", value);
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.SETTER)
    public void setUnit(B2BUnitModel value)
    {
        getPersistenceContext().setPropertyValue("Unit", value);
    }
}
