package de.hybris.platform.core.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class OrderEntryModel extends AbstractOrderEntryModel
{
    public static final String _TYPECODE = "OrderEntry";
    public static final String _ABSTRACTORDER2ABSTRACTORDERENTRY = "AbstractOrder2AbstractOrderEntry";


    public OrderEntryModel()
    {
    }


    public OrderEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderEntryModel(ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public OrderEntryModel(ItemModel _owner, ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setOwner(_owner);
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public OrderModel getOrder()
    {
        return (OrderModel)super.getOrder();
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(AbstractOrderModel value)
    {
        if(value == null || value instanceof OrderModel)
        {
            super.setOrder(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.core.model.order.OrderModel");
        }
    }
}
