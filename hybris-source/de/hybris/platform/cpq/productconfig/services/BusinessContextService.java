/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services;

import de.hybris.platform.cpq.productconfig.services.data.BusinessContext;

/**
 * Handling of business attributes for CPQ
 */
public interface BusinessContextService
{
    /**
     * Determines the CPQ relevant business context for the current user
     *
     * @return business context
     */
    BusinessContext getBusinessContext();


    /**
     * Determines the owner id
     *
     * @return owner id
     */
    String getOwnerId();


    /**
     * Sends business context to CPQ defaulting the owner id and business context from the interface methods
     */
    default void sendBusinessContextToCPQ()
    {
        sendBusinessContextToCPQ(getOwnerId(), getBusinessContext());
    }


    /**
     * Sends business context to CPQ for a given owner id
     *
     * @param ownerId
     *           owner id
     * @param businessContext
     *           business context
     */
    void sendBusinessContextToCPQ(String ownerId, BusinessContext businessContext);
}
