/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.retail.sapppspricing;

import com.sap.retail.opps.v1.dto.PriceCalculateBase;
import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * Validator to ensure that a former result of a price calculation can still be used
 *
 */
public interface CalculationResultValidator
{
    /**
     * @param priceCalculate
     *           Result of former price calculation
     * @param order
     *           current order / cart
     * @return true if result still represents the price calculation for a part of the current order / cart
     */
    boolean validate(PriceCalculateBase priceCalculate, AbstractOrderModel order);
}
