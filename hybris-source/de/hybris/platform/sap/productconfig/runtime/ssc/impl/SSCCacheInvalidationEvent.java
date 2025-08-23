/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.PublishEventContext;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import java.util.Collections;
import java.util.List;

/**
 * ClusterAwareEvent object created from dataloader instance encapsulating list of keys to be invalidated and the name
 * of the cache region from where the invalidation needs to be performed
 */
public class SSCCacheInvalidationEvent extends AbstractEvent implements ClusterAwareEvent
{
    private static final long serialVersionUID = 1L;
    private final List<String> cacheInvalidationKeys;
    private final String cacheRegionName;


    public SSCCacheInvalidationEvent(final List<String> cacheInvalidationKeys, final String cacheRegionName)
    {
        super();
        this.cacheInvalidationKeys = List.copyOf(cacheInvalidationKeys);
        this.cacheRegionName = cacheRegionName;
    }


    public List<String> getCacheInvalidationKeys()
    {
        return Collections.unmodifiableList(cacheInvalidationKeys);
    }


    public String getCacheRegionName()
    {
        return cacheRegionName;
    }


    @Override
    public boolean canPublish(final PublishEventContext publishEventContext)
    {
        return true;
    }
}
