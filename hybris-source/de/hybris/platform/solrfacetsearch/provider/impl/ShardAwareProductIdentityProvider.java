package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.provider.IdentityProvider;
import de.hybris.platform.variants.model.VariantProductModel;
import java.io.Serializable;

public class ShardAwareProductIdentityProvider extends AbstractProductIdentityProvider implements IdentityProvider<ProductModel>, Serializable
{
    public static final String GROUPING_SEPARATOR = "!";


    public String getIdentifier(IndexConfig indexConfig, ProductModel product)
    {
        StringBuilder sb = new StringBuilder();
        if(product instanceof VariantProductModel)
        {
            sb.append(getTopBaseProductCode(product)).append("!");
        }
        sb.append(getIdentifierForProduct(product));
        return sb.toString();
    }


    protected String getTopBaseProductCode(ProductModel product)
    {
        if(product instanceof VariantProductModel)
        {
            ProductModel baseProduct = ((VariantProductModel)product).getBaseProduct();
            return getTopBaseProductCode(baseProduct);
        }
        return product.getCode();
    }
}
