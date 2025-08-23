/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataquality;

import com.hybris.cockpitng.dataquality.model.DataQuality;
import java.util.Optional;

/**
 * This service provides functionality to calculate the state of an item in terms of data coverage. An item (e.g.
 * Product) with lots of unfilled important attributes has low coverage. An item with all important attributes filled
 * with correct data has high coverage. How this coverage information is calculated, which attributes are taking into
 * account and if semantic of the data is considered as well is up to the implementation.
 */
public interface DataQualityCalculationService
{
    /**
     * Calculates the coverage of the given object.
     * The domainId can be used to distinguish between multiple
     * coverage calculation strategies per domain (e.g. text translation coverage,
     * print related coverage etc.)
     *
     * @param object
     *           the object to calculate the coverage for
     * @param domainId
     *           the domain Id if applicable
     * @return the <code>Optional&lt;DataQuality&gt;</code> object with cumulated coverage information or
     *         <code>Optional.empty()</code> value if no strategy was registered for given object and domain ID
     */
    Optional<DataQuality> calculate(Object object, String domainId);


    /**
     * Calculates the coverage of the given object.
     * The domainId can be used to distinguish between multiple
     * coverage calculation strategies per domain (e.g. text translation coverage,
     * print related coverage etc.)
     *
     * @param object
     *           the object to calculate the coverage for
     * @param templateCode
     *           the object template to be used for finding the proper calculation strategy
     * @param domainId
     *           the domain Id if applicable
     * @return the <code>Optional&lt;DataQuality&gt;</code> object with cumulated coverage information or
     *         <code>Optional.empty()</code> if no strategy
     *         was registered for given object and domain ID
     */
    Optional<DataQuality> calculate(Object object, String templateCode, String domainId);
}
