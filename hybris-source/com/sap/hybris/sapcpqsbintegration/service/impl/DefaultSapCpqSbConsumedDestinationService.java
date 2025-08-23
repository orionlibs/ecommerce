/*
 * [y] hybris Platform
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.sap.hybris.sapcpqsbintegration.service.impl;

import com.sap.hybris.sapcpqsbintegration.service.SapCpqSbConsumedDestinationService;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.log4j.Logger;

/**
 *
 *  Default implementation for SapCpqSbConsumedDestinationService
 *
 */
public class DefaultSapCpqSbConsumedDestinationService implements SapCpqSbConsumedDestinationService
{
    private static final Logger LOG = Logger.getLogger(DefaultSapCpqSbConsumedDestinationService.class);
    private static final String ERROR_FINDING_DESTINATION = "Failed to find consumed destination with id :";
    private FlexibleSearchService flexibleSearchService;


    public boolean checkIfDestinationExists(String destinationId)
    {
        boolean isExists = true;
        final ConsumedDestinationModel destinationModel = getConsumedDestination(destinationId);
        if(destinationModel == null)
        {
            isExists = false;
        }
        return isExists;
    }


    private ConsumedDestinationModel getConsumedDestination(String destinationId)
    {
        ConsumedDestinationModel destination = null;
        try
        {
            final ConsumedDestinationModel consumedDestinationModel = new ConsumedDestinationModel();
            consumedDestinationModel.setId(destinationId);
            destination = getFlexibleSearchService().getModelByExample(consumedDestinationModel);
        }
        catch(final ModelNotFoundException e)
        {
            LOG.error(ERROR_FINDING_DESTINATION + destinationId + e.getMessage());
        }
        return destination;
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return flexibleSearchService;
    }


    public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
