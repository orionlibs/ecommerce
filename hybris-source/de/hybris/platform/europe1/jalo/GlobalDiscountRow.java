package de.hybris.platform.europe1.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;

public class GlobalDiscountRow extends GeneratedGlobalDiscountRow
{
    @SLDSafe(portingClass = "de.hybris.platform.product.impl.GlobalDiscountRowPrepareInterceptor", portingMethod = "onPrepare")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.remove("product");
        allAttributes.remove("pg");
        return super.createItem(ctx, type, allAttributes);
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.GlobalDiscountRowPrepareInterceptor", portingMethod = "onPrepare")
    protected void setProduct(SessionContext ctx, Product value)
    {
    }


    @SLDSafe(portingClass = "de.hybris.platform.product.impl.GlobalDiscountRowPrepareInterceptor", portingMethod = "onPrepare")
    protected void setPg(SessionContext ctx, EnumerationValue value)
    {
    }
}
