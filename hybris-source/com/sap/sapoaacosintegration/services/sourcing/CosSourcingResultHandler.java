/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.sapoaacosintegration.services.sourcing;

import com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.response.SourcingResponse;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * Result handler for COS sourcing response
 */
public interface CosSourcingResultHandler
{
    /**
     * @param response
     * @param model
     */
    void persistCosSourcingResultInCart(SourcingResponse response, AbstractOrderModel model);


    /**
     * @param model
     */
    void populateScheduleLinesForPickupProduct(AbstractOrderEntryModel model);
}
