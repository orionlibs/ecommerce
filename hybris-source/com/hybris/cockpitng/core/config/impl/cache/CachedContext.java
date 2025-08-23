/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.cache;

import com.hybris.cockpitng.core.config.impl.jaxb.Context;

/**
 * Interface for {@link Context} that is wrapped and cached.
 */
public interface CachedContext
{
    /**
     * Gets an original context that was wrapped and cached
     *
     * @return original context
     */
    Context getOriginal();
}
