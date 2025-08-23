package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class B2BPermissionModel extends ItemModel
{
    public static final String _TYPECODE = "B2BPermission";
    public static final String _B2BUSERGROUPS2B2BPERMISSIONS = "B2BUserGroups2B2BPermissions";
    public static final String _B2BUNIT2B2BPERMISSIONS = "B2BUnit2B2BPermissions";
    public static final String _B2BCUSTOMER2B2BPERMISSIONS = "B2BCustomer2B2BPermissions";
    public static final String CODE = "code";
    public static final String ACTIVE = "active";
    public static final String MESSAGE = "message";
    public static final String USERGROUPS = "UserGroups";
    public static final String UNITPOS = "UnitPOS";
    public static final String UNIT = "Unit";
    public static final String CUSTOMERS = "Customers";


    public B2BPermissionModel()
    {
    }


    public B2BPermissionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BPermissionModel(B2BUnitModel _Unit, String _code)
    {
        setUnit(_Unit);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BPermissionModel(B2BUnitModel _Unit, String _code, ItemModel _owner)
    {
        setUnit(_Unit);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "Customers", type = Accessor.Type.GETTER)
    public Collection<B2BCustomerModel> getCustomers()
    {
        return (Collection<B2BCustomerModel>)getPersistenceContext().getPropertyValue("Customers");
    }


    @Accessor(qualifier = "message", type = Accessor.Type.GETTER)
    public String getMessage()
    {
        return getMessage(null);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.GETTER)
    public String getMessage(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("message", loc);
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.GETTER)
    public B2BUnitModel getUnit()
    {
        return (B2BUnitModel)getPersistenceContext().getPropertyValue("Unit");
    }


    @Accessor(qualifier = "UserGroups", type = Accessor.Type.GETTER)
    public List<B2BUserGroupModel> getUserGroups()
    {
        return (List<B2BUserGroupModel>)getPersistenceContext().getPropertyValue("UserGroups");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "Customers", type = Accessor.Type.SETTER)
    public void setCustomers(Collection<B2BCustomerModel> value)
    {
        getPersistenceContext().setPropertyValue("Customers", value);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.SETTER)
    public void setMessage(String value)
    {
        setMessage(value, null);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.SETTER)
    public void setMessage(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("message", loc, value);
    }


    @Accessor(qualifier = "Unit", type = Accessor.Type.SETTER)
    public void setUnit(B2BUnitModel value)
    {
        getPersistenceContext().setPropertyValue("Unit", value);
    }


    @Accessor(qualifier = "UserGroups", type = Accessor.Type.SETTER)
    public void setUserGroups(List<B2BUserGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("UserGroups", value);
    }
}
