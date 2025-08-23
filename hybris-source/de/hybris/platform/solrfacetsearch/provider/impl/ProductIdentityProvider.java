package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.provider.IdentityProvider;
import java.io.Serializable;

public class ProductIdentityProvider extends AbstractProductIdentityProvider implements IdentityProvider<ProductModel>, Serializable
{
    public String getIdentifier(IndexConfig indexConfig, ProductModel model)
    {
        return getIdentifierForProduct(model);
    }
}
