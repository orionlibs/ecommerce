package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RegularCustomerOrderQuantityRestrictionModel extends RestrictionModel
{
    public static final String _TYPECODE = "RegularCustomerOrderQuantityRestriction";
    public static final String ORDERQUANTITY = "orderQuantity";


    public RegularCustomerOrderQuantityRestrictionModel()
    {
    }


    public RegularCustomerOrderQuantityRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RegularCustomerOrderQuantityRestrictionModel(Integer _orderQuantity, VoucherModel _voucher)
    {
        setOrderQuantity(_orderQuantity);
        setVoucher(_voucher);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RegularCustomerOrderQuantityRestrictionModel(Integer _orderQuantity, ItemModel _owner, VoucherModel _voucher)
    {
        setOrderQuantity(_orderQuantity);
        setOwner(_owner);
        setVoucher(_voucher);
    }


    @Accessor(qualifier = "orderQuantity", type = Accessor.Type.GETTER)
    public Integer getOrderQuantity()
    {
        return (Integer)getPersistenceContext().getPropertyValue("orderQuantity");
    }


    @Accessor(qualifier = "orderQuantity", type = Accessor.Type.SETTER)
    public void setOrderQuantity(Integer value)
    {
        getPersistenceContext().setPropertyValue("orderQuantity", value);
    }
}
