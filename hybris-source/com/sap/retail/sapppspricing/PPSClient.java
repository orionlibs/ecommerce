/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.retail.sapppspricing;

import com.sap.retail.opps.v1.dto.PriceCalculate;
import com.sap.retail.opps.v1.dto.PriceCalculateResponse;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;

/**
 * Client for the Promotion Pricing Service
 *
 */
public interface PPSClient
{
    /**
     * Call promotion pricing service
     *
     * @param priceCalculate
     *           {@link com.sap.retail.opps.v1.dto.PriceCalculate}
     * @param sapConfig
     *           SAP base store configuration
     * @return {@link com.sap.retail.opps.v1.dto.PriceCalculateResponse}
     *
     */
    PriceCalculateResponse callPPS(PriceCalculate priceCalculate, SAPConfigurationModel sapConfig);
}
