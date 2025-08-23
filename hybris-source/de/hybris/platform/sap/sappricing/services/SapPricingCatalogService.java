/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sappricing.services;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import java.util.List;

/**
 *	Sap Pricing Catalog service.
 */
public interface SapPricingCatalogService extends PriceService
{
    /**
     * Method to get price information for products
     *
     * @param models List<ProductModel>
     * @return List<PriceInformation>
     */
    public List<PriceInformation> getPriceInformationForProducts(List<ProductModel> models);
}
