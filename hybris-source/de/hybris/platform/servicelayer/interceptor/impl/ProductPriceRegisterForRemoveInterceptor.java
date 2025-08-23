package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;

public class ProductPriceRegisterForRemoveInterceptor extends PartOfModelRegisterForRemoveInterceptor
{
    protected void registerForRemoval(InterceptorContext ctx, ItemModel parentModel, ItemModel subModel, String partOfAttrQualifier)
    {
        if(subModel instanceof PDTRowModel && parentModel instanceof de.hybris.platform.core.model.product.ProductModel && ("europe1Prices"
                        .equalsIgnoreCase(partOfAttrQualifier) || "europe1Taxes"
                        .equalsIgnoreCase(partOfAttrQualifier) || "europe1Discounts"
                        .equalsIgnoreCase(partOfAttrQualifier)))
        {
            PDTRowModel pdtrow = (PDTRowModel)subModel;
            if(parentModel.equals(pdtrow.getProduct()))
            {
                super.registerForRemoval(ctx, parentModel, subModel, partOfAttrQualifier);
            }
        }
        else
        {
            super.registerForRemoval(ctx, parentModel, subModel, partOfAttrQualifier);
        }
    }
}
