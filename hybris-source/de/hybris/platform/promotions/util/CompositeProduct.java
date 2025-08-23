package de.hybris.platform.promotions.util;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;

public interface CompositeProduct
{
    Product getCompositeParentProduct(SessionContext paramSessionContext);
}
