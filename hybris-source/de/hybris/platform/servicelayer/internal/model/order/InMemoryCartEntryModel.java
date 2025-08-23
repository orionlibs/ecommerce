package de.hybris.platform.servicelayer.internal.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class InMemoryCartEntryModel extends CartEntryModel
{
    public static final String _TYPECODE = "InMemoryCartEntry";
    public static final String _ABSTRACTORDER2ABSTRACTORDERENTRY = "AbstractOrder2AbstractOrderEntry";


    public InMemoryCartEntryModel()
    {
    }


    public InMemoryCartEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public InMemoryCartEntryModel(ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public InMemoryCartEntryModel(ItemModel _owner, ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setOwner(_owner);
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public InMemoryCartModel getOrder()
    {
        return (InMemoryCartModel)super.getOrder();
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(AbstractOrderModel value)
    {
        if(value == null || value instanceof InMemoryCartModel)
        {
            super.setOrder(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.servicelayer.internal.model.order.InMemoryCartModel");
        }
    }
}
