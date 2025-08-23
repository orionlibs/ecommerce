package de.hybris.platform.returns.model;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ReturnOrderEntryModel extends OrderEntryModel
{
    public static final String _TYPECODE = "ReturnOrderEntry";


    public ReturnOrderEntryModel()
    {
    }


    public ReturnOrderEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReturnOrderEntryModel(ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReturnOrderEntryModel(ItemModel _owner, ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setOwner(_owner);
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }
}
