package de.hybris.platform.ordersplitting.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import java.util.Iterator;

public class StockLevel extends GeneratedStockLevel
{
    @Deprecated(since = "ages", forRemoval = true)
    public Product getProduct(SessionContext ctx)
    {
        Iterator<Product> iterator = getProducts().iterator();
        if(iterator.hasNext())
        {
            Product product = iterator.next();
            return product;
        }
        return null;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setProduct(SessionContext ctx, Product value)
    {
        if(null != value && !getProducts(ctx).contains(value))
        {
            addToProducts(value);
        }
    }
}
