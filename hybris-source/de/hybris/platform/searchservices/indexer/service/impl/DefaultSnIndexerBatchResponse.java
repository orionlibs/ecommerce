/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchResponse;

/**
 * Default implementation for {@link SnIndexerBatchResponse}.
 */
public class DefaultSnIndexerBatchResponse extends DefaultSnIndexerResponse implements SnIndexerBatchResponse
{
    public DefaultSnIndexerBatchResponse(final SnIndexConfiguration indexConfiguration, final SnIndexType indexType)
    {
        super(indexConfiguration, indexType);
    }
}
