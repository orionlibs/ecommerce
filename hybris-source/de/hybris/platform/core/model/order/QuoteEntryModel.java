package de.hybris.platform.core.model.order;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class QuoteEntryModel extends AbstractOrderEntryModel
{
    public static final String _TYPECODE = "QuoteEntry";
    public static final String _ABSTRACTORDER2ABSTRACTORDERENTRY = "AbstractOrder2AbstractOrderEntry";


    public QuoteEntryModel()
    {
    }


    public QuoteEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public QuoteEntryModel(ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public QuoteEntryModel(ItemModel _owner, ProductModel _product, Long _quantity, UnitModel _unit)
    {
        setOwner(_owner);
        setProduct(_product);
        setQuantity(_quantity);
        setUnit(_unit);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public QuoteModel getOrder()
    {
        return (QuoteModel)super.getOrder();
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(AbstractOrderModel value)
    {
        if(value == null || value instanceof QuoteModel)
        {
            super.setOrder(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.core.model.order.QuoteModel");
        }
    }
}
