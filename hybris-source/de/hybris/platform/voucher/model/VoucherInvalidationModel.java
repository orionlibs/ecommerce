package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class VoucherInvalidationModel extends ItemModel
{
    public static final String _TYPECODE = "VoucherInvalidation";
    public static final String _VOUCHERINVALIDATIONSRELATION = "VoucherInvalidationsRelation";
    public static final String CODE = "code";
    public static final String USER = "user";
    public static final String ORDER = "order";
    public static final String STATUS = "status";
    public static final String VOUCHER = "voucher";


    public VoucherInvalidationModel()
    {
    }


    public VoucherInvalidationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VoucherInvalidationModel(String _code, OrderModel _order, UserModel _user, VoucherModel _voucher)
    {
        setCode(_code);
        setOrder(_order);
        setUser(_user);
        setVoucher(_voucher);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public VoucherInvalidationModel(String _code, OrderModel _order, ItemModel _owner, UserModel _user, VoucherModel _voucher)
    {
        setCode(_code);
        setOrder(_order);
        setOwner(_owner);
        setUser(_user);
        setVoucher(_voucher);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public OrderModel getOrder()
    {
        return (OrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public String getStatus()
    {
        return (String)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "voucher", type = Accessor.Type.GETTER)
    public VoucherModel getVoucher()
    {
        return (VoucherModel)getPersistenceContext().getPropertyValue("voucher");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(OrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(String value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }


    @Accessor(qualifier = "voucher", type = Accessor.Type.SETTER)
    public void setVoucher(VoucherModel value)
    {
        getPersistenceContext().setPropertyValue("voucher", value);
    }
}
