/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.strategies.impl;

import de.hybris.platform.apiregistryservices.dto.RegisteredDestinationData;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.apiregistryservices.strategies.ApiRegistrationStrategy;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of @{@link ApiRegistrationStrategy}
 */
public class DefaultApiRegistrationStrategy implements ApiRegistrationStrategy
{
    @Override
    public void registerExposedDestination(final ExposedDestinationModel destination) throws ApiRegistrationException
    {
        // empty
    }


    @Override
    public void unregisterExposedDestination(final ExposedDestinationModel destination) throws ApiRegistrationException
    {
        // empty
    }


    @Override
    public List<RegisteredDestinationData> retrieveRegisteredExposedDestinations(final DestinationTargetModel destinationTarget)
                    throws ApiRegistrationException
    {
        return Collections.emptyList();
    }


    @Override
    public void unregisterExposedDestinationByTargetId(final String targetId, final String destinationTargetId) throws ApiRegistrationException
    {
        // empty
    }
}
