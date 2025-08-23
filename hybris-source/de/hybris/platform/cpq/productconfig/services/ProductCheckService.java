/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services;

import java.util.Set;

/**
 * Checks for the availability of product IDs in commerce
 *
 * @deprecated Since 2108.
 */
@Deprecated(since = "2108", forRemoval = true)
public interface ProductCheckService
{
    /**
     * Checks a list of ID's for their availability in commerce
     *
     * @param productIds
     *           ID's to check
     * @return Set that contains the ID's that are available
     */
    Set<String> checkProductIds(Set<String> productIds);
}
