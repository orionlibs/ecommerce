package de.hybris.platform.commerceservices.model.promotions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.model.AbstractPromotionRestrictionModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class PromotionOrderRestrictionModel extends AbstractPromotionRestrictionModel
{
    public static final String _TYPECODE = "PromotionOrderRestriction";
    public static final String _PROMOTIONRESTRICTION2ORDERREL = "PromotionRestriction2OrderRel";
    public static final String ORDERS = "orders";


    public PromotionOrderRestrictionModel()
    {
    }


    public PromotionOrderRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionOrderRestrictionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "orders", type = Accessor.Type.GETTER)
    public Collection<AbstractOrderModel> getOrders()
    {
        return (Collection<AbstractOrderModel>)getPersistenceContext().getPropertyValue("orders");
    }


    @Accessor(qualifier = "orders", type = Accessor.Type.SETTER)
    public void setOrders(Collection<AbstractOrderModel> value)
    {
        getPersistenceContext().setPropertyValue("orders", value);
    }
}
