/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services.strategies;

import de.hybris.platform.cpq.productconfig.services.client.CpqClient;

/**
 * Facilitats interaction with CPQ
 */
public interface CPQInteractionStrategy
{
    /**
     * Retrieves a CPQ client to interact with
     *
     * @return CPQ client
     */
    CpqClient getClient();


    /**
     * Determines the admin authorization string for interaction with the CPQ client
     *
     * @return authorization string
     */
    String getAuthorizationString();


    /**
     * Determines the client authorization string for a given owenerId for interaction with the CPQ client
     *
     * @param ownerId
     *           ownerId which will be encoded in the token
     * @return authorization string
     */
    String getClientAuthorizationString(String ownerId);
}
