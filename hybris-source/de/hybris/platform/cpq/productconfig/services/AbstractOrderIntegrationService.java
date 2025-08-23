/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

/**
 * Deals with abstract order integration
 */
public interface AbstractOrderIntegrationService
{
    /**
     * Retrieve configuration ID for abstract order entry
     *
     * @param entry
     *           Abstract order entry
     * @return Configuration ID
     */
    String getConfigIdForAbstractOrderEntry(AbstractOrderEntryModel entry);


    /**
     * * Save new configuration ID for abstract order entry
     *
     * @param entry
     *           Abstract order entry
     * @param newConfigId
     *           new configuration ID
     *
     */
    void setConfigIdForAbstractOrderEntry(AbstractOrderEntryModel entry, String newConfigId);
}
