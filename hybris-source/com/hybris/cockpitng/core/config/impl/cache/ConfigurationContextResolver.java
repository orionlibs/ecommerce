/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.cache;

import com.hybris.cockpitng.core.config.impl.jaxb.Context;
import java.util.Map;

/**
 * An interface able to extract all attributes and their values from specified context. Interface should take all
 * possible context inheritance, merging, etc. for the returned map to be a map of final effective attributes of
 * context.
 */
public interface ConfigurationContextResolver
{
    /**
     * Retrieves all attributes and their values for context
     *
     * @param context context for which attributes are required
     * @return map of effective attributes of provided context in form of &gt;name, value&lt;
     */
    Map<String, String> getContextAttributes(final Context context);
}
