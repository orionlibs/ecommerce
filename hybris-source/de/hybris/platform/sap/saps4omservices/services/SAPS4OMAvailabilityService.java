/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saps4omservices.services;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.saps4omservices.exceptions.OutboundServiceException;
import de.hybris.platform.store.BaseStoreModel;

/**
 *
 * Availability service provides the availability and future stock details.
 *
 */
public interface SAPS4OMAvailabilityService
{
    /**
     * Get the SapS4OMProductAvailability (contains stock , future stock details) for
     * the product and the given basestore
     *
     * @param productModel   the ProductModel
     * @param baseStore the baseStoreModel
     * @return SapS4OMProductAvailability for the corresponding product and warehouse.
     * @throws de.hybris.platform.sap.saps4omservices.exceptions.OutboundServiceException
     */
    SapS4OMProductAvailability getProductAvailability(ProductModel productModel, BaseStoreModel baseStore) throws OutboundServiceException;
}
