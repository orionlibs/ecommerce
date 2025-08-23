package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;

public class AbstractProductIdentityProvider
{
    public String getIdentifierForProduct(ProductModel product)
    {
        CatalogVersionModel catalogVersion = product.getCatalogVersion();
        String code = product.getCode();
        return catalogVersion.getCatalog().getId() + "/" + catalogVersion.getCatalog().getId() + "/" + catalogVersion.getVersion();
    }
}
