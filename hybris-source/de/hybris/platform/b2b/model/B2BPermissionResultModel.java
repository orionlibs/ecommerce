package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.b2b.enums.PermissionStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class B2BPermissionResultModel extends ItemModel
{
    public static final String _TYPECODE = "B2BPermissionResult";
    public static final String _ABSTRACTORDER2B2BPERMISSIONRESULTS = "AbstractOrder2B2BPermissionResults";
    public static final String PERMISSION = "permission";
    public static final String PERMISSIONTYPECODE = "permissionTypeCode";
    public static final String STATUS = "status";
    public static final String APPROVER = "approver";
    public static final String NOTE = "note";
    public static final String STATUSDISPLAY = "statusDisplay";
    public static final String ORDERPOS = "OrderPOS";
    public static final String ORDER = "Order";


    public B2BPermissionResultModel()
    {
    }


    public B2BPermissionResultModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BPermissionResultModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "approver", type = Accessor.Type.GETTER)
    public B2BCustomerModel getApprover()
    {
        return (B2BCustomerModel)getPersistenceContext().getPropertyValue("approver");
    }


    @Accessor(qualifier = "note", type = Accessor.Type.GETTER)
    public String getNote()
    {
        return getNote(null);
    }


    @Accessor(qualifier = "note", type = Accessor.Type.GETTER)
    public String getNote(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("note", loc);
    }


    @Accessor(qualifier = "Order", type = Accessor.Type.GETTER)
    public AbstractOrderModel getOrder()
    {
        return (AbstractOrderModel)getPersistenceContext().getPropertyValue("Order");
    }


    @Accessor(qualifier = "permission", type = Accessor.Type.GETTER)
    public B2BPermissionModel getPermission()
    {
        return (B2BPermissionModel)getPersistenceContext().getPropertyValue("permission");
    }


    @Accessor(qualifier = "permissionTypeCode", type = Accessor.Type.GETTER)
    public String getPermissionTypeCode()
    {
        return (String)getPersistenceContext().getPropertyValue("permissionTypeCode");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public PermissionStatus getStatus()
    {
        return (PermissionStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "statusDisplay", type = Accessor.Type.GETTER)
    public String getStatusDisplay()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "statusDisplay");
    }


    @Accessor(qualifier = "approver", type = Accessor.Type.SETTER)
    public void setApprover(B2BCustomerModel value)
    {
        getPersistenceContext().setPropertyValue("approver", value);
    }


    @Accessor(qualifier = "note", type = Accessor.Type.SETTER)
    public void setNote(String value)
    {
        setNote(value, null);
    }


    @Accessor(qualifier = "note", type = Accessor.Type.SETTER)
    public void setNote(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("note", loc, value);
    }


    @Accessor(qualifier = "Order", type = Accessor.Type.SETTER)
    public void setOrder(AbstractOrderModel value)
    {
        getPersistenceContext().setPropertyValue("Order", value);
    }


    @Accessor(qualifier = "permission", type = Accessor.Type.SETTER)
    public void setPermission(B2BPermissionModel value)
    {
        getPersistenceContext().setPropertyValue("permission", value);
    }


    @Accessor(qualifier = "permissionTypeCode", type = Accessor.Type.SETTER)
    public void setPermissionTypeCode(String value)
    {
        getPersistenceContext().setPropertyValue("permissionTypeCode", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(PermissionStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }
}
