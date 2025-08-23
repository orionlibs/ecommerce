package de.hybris.platform.core.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CartEntryModel extends AbstractOrderEntryModel
{
    public static final String _TYPECODE = "CartEntry";
    public static final String _ABSTRACTORDER2ABSTRACTORDERENTRY = "AbstractOrder2AbstractOrderEntry";


    public CartEntryModel()
    {
    }


    public CartEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CartEntryModel(ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CartEntryModel(ItemModel _owner, ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setOwner(_owner);
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public CartModel getOrder()
    {
        return (CartModel)super.getOrder();
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(AbstractOrderModel value)
    {
        if(value == null || value instanceof CartModel)
        {
            super.setOrder(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.core.model.order.CartModel");
        }
    }
}
