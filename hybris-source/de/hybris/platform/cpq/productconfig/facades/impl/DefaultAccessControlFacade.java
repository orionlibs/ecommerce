/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.facades.impl;

import de.hybris.platform.cpq.productconfig.facades.AccessControlFacade;
import de.hybris.platform.cpq.productconfig.facades.data.AccessControlData;
import de.hybris.platform.cpq.productconfig.services.AuthorizationService;
import de.hybris.platform.cpq.productconfig.services.BusinessContextService;
import de.hybris.platform.cpq.productconfig.services.data.AuthorizationData;

/**
 * Default implementation of {@link AccessControlFacade}
 */
public class DefaultAccessControlFacade implements AccessControlFacade
{
    private final BusinessContextService businessContextService;
    private final AuthorizationService authorizationService;


    /**
     * Default constructor
     *
     * @param businessContextService
     *           business context service
     * @param authorizationService
     *           authorization service
     */
    public DefaultAccessControlFacade(
                    final BusinessContextService businessContextService,
                    final AuthorizationService authorizationService)
    {
        this.businessContextService = businessContextService;
        this.authorizationService = authorizationService;
    }


    @Override
    public AccessControlData performAccessControlForClient()
    {
        getBusinessContextService().sendBusinessContextToCPQ();
        final AuthorizationData authorizationDataForClient = getAuthorizationService()
                        .getAuthorizationDataForClient(getBusinessContextService().getOwnerId());
        return convert(authorizationDataForClient);
    }


    protected AccessControlData convert(final AuthorizationData authorizationDataForClient)
    {
        final AccessControlData accessControlData = new AccessControlData();
        accessControlData.setServiceEndpointUrl(authorizationDataForClient.getServiceEndpointUrl());
        accessControlData.setAccessToken(authorizationDataForClient.getAccessToken());
        accessControlData.setAccessTokenExpiration(authorizationDataForClient.getExpiresAt());
        accessControlData.setOwnerId(authorizationDataForClient.getOwnerId());
        return accessControlData;
    }


    protected BusinessContextService getBusinessContextService()
    {
        return businessContextService;
    }


    protected AuthorizationService getAuthorizationService()
    {
        return authorizationService;
    }
}
