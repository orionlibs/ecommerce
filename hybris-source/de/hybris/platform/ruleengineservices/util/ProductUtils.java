package de.hybris.platform.ruleengineservices.util;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.HashSet;
import java.util.Set;

public class ProductUtils
{
    public Set<ProductModel> getAllBaseProducts(ProductModel productModel)
    {
        Set<ProductModel> allBaseProducts = new HashSet<>();
        ProductModel currentProduct = productModel;
        while(currentProduct instanceof VariantProductModel)
        {
            currentProduct = ((VariantProductModel)currentProduct).getBaseProduct();
            if(currentProduct == null)
            {
                break;
            }
            allBaseProducts.add(currentProduct);
        }
        return allBaseProducts;
    }
}
