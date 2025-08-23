/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.cps;

import de.hybris.platform.sap.productconfig.runtime.cps.impl.CharonFacadeImpl;

/**
 * Retrieving an SAP passport in the context of product configuration
 */
public interface ProductConfigurationPassportService
{
    /**
     * Generates an SAP Passport based on the provided info.
     *
     * @param actionId
     *           Will be set as action in SAP passport. In our case, we specify the type of the microservice call (e.g.
     *           {@link CharonFacadeImpl#PASSPORT_GET_CONFIG} or {@link CharonFacadeImpl#PASSPORT_UPDATE_CONFIG}
     * @return The passport as String
     */
    String generate(String actionId);
}
