package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ProductQuantityRestrictionModel extends ProductRestrictionModel
{
    public static final String _TYPECODE = "ProductQuantityRestriction";
    public static final String QUANTITY = "quantity";
    public static final String UNIT = "unit";


    public ProductQuantityRestrictionModel()
    {
    }


    public ProductQuantityRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductQuantityRestrictionModel(Long _quantity, UnitModel _unit, VoucherModel _voucher)
    {
        setQuantity(_quantity);
        setUnit(_unit);
        setVoucher(_voucher);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductQuantityRestrictionModel(ItemModel _owner, Long _quantity, UnitModel _unit, VoucherModel _voucher)
    {
        setOwner(_owner);
        setQuantity(_quantity);
        setUnit(_unit);
        setVoucher(_voucher);
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.GETTER)
    public Long getQuantity()
    {
        return (Long)getPersistenceContext().getPropertyValue("quantity");
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
    public UnitModel getUnit()
    {
        return (UnitModel)getPersistenceContext().getPropertyValue("unit");
    }


    @Accessor(qualifier = "quantity", type = Accessor.Type.SETTER)
    public void setQuantity(Long value)
    {
        getPersistenceContext().setPropertyValue("quantity", value);
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
    public void setUnit(UnitModel value)
    {
        getPersistenceContext().setPropertyValue("unit", value);
    }
}
