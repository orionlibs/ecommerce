/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services;

import de.hybris.platform.cpq.productconfig.services.cache.DefaultCacheKey;

/**
 * Generates cache keys to be used for hybris cache regions
 */
public interface CacheKeyService
{
    /**
     * Creates a cache key for the authorization data cache region where the base site is set externally
     *
     * @param baseSite
     *           base site
     *
     * @return the created cache key
     */
    DefaultCacheKey createAuthorizationDataCacheKey(final String baseSite);


    /**
     * Creates a cache key for the authorization data cache region
     *
     * @return the created cache key
     */
    DefaultCacheKey createAuthorizationDataCacheKey();


    /**
     * Creates a cache key for the configuration summary cache region
     *
     * @param configId
     *           identifier of the configuration summary
     *
     * @return the created cache key
     */
    DefaultCacheKey createConfigurationSummaryCacheKey(final String configId);
}
