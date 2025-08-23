package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class PromotionOrderAdjustTotalActionModel extends AbstractPromotionActionModel
{
    public static final String _TYPECODE = "PromotionOrderAdjustTotalAction";
    public static final String AMOUNT = "amount";


    public PromotionOrderAdjustTotalActionModel()
    {
    }


    public PromotionOrderAdjustTotalActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionOrderAdjustTotalActionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.GETTER)
    public Double getAmount()
    {
        return (Double)getPersistenceContext().getPropertyValue("amount");
    }


    @Accessor(qualifier = "amount", type = Accessor.Type.SETTER)
    public void setAmount(Double value)
    {
        getPersistenceContext().setPropertyValue("amount", value);
    }
}
