package de.hybris.platform.europe1.model;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class GlobalDiscountRowModel extends AbstractDiscountRowModel
{
    public static final String _TYPECODE = "GlobalDiscountRow";


    public GlobalDiscountRowModel()
    {
    }


    public GlobalDiscountRowModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public GlobalDiscountRowModel(DiscountModel _discount)
    {
        setDiscount(_discount);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public GlobalDiscountRowModel(DiscountModel _discount, ItemModel _owner, ProductDiscountGroup _pg, ProductModel _product, String _productId)
    {
        setDiscount(_discount);
        setOwner(_owner);
        setPg((HybrisEnumValue)_pg);
        setProduct(_product);
        setProductId(_productId);
    }
}
