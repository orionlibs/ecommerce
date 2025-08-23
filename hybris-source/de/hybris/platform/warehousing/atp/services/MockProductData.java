package de.hybris.platform.warehousing.atp.services;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;

class MockProductData extends ProductModel
{
    public ItemModelInternalContext getPersistenceContextTesting()
    {
        return getPersistenceContext();
    }
}
