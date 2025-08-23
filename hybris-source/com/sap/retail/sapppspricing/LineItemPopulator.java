/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.retail.sapppspricing;

import com.sap.retail.opps.v1.dto.LineItemDomainSpecific;
import org.springframework.core.Ordered;

/**
 * Generic populator for a PriceCalculate request line item
 *
 * @param <T>
 *           source type to use
 *
 */
public interface LineItemPopulator<T> extends Ordered
{
    /**
     * @param lineItem
     *           line item to populate
     * @param source
     */
    void populate(LineItemDomainSpecific lineItem, T source);
}
