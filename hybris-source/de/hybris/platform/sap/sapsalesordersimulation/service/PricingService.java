/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapsalesordersimulation.service;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import java.util.List;

/**
 * Get the price information for the  product
 *
 */
public interface PricingService
{
    /**
     * Get the Price information for the product
     *
     * @param product the ProductModel
     * l
     * @return list of PriceInformation for the corresponding product
     */
    List<PriceInformation> getPriceForProduct(ProductModel product);
}
