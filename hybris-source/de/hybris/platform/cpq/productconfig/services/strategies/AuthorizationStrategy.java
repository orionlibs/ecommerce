/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services.strategies;

import de.hybris.platform.cpq.productconfig.services.data.AuthorizationData;
import de.hybris.platform.cpq.productconfig.services.data.CpqCredentialsData;

/**
 * Creates authorization data and endpoint information
 */
public interface AuthorizationStrategy
{
    /**
     * @param credentials
     * @return Authorization Data
     */
    AuthorizationData getAuthorizationData(final CpqCredentialsData credentials);


    /**
     * @return Credentials for connecting to CPQ from the Commerce server
     */
    CpqCredentialsData getCpqCredentialsForAdmin();


    /**
     * @param ownerId
     *           owner id of the client
     * @return Credentials for connecting to CPQ from a client
     */
    CpqCredentialsData getCpqCredentialsForClient(String ownerId);


    /**
     * @return a new token will be requested already the specified amount of ms before the token expires.
     */
    long getTokenExpirationBuffer();
}
