/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.sync.updaters;

import com.google.common.collect.Lists;
import com.hybris.backoffice.events.sync.SyncStartEvent;
import com.hybris.backoffice.widgets.processes.updater.ProcessesUpdater;
import com.hybris.cockpitng.core.events.CockpitEvent;
import de.hybris.platform.servicelayer.event.events.BeforeCronJobStartEvent;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @deprecated since 6.6, no longer used.
 */
@Deprecated(since = "6.6", forRemoval = true)
public class SyncStartedUpdater implements ProcessesUpdater
{
    @Override
    public String getEventName()
    {
        return SyncStartEvent.EVENT_NAME;
    }


    @Override
    public List<String> onEvent(final CockpitEvent cockpitEvent)
    {
        if(cockpitEvent.getData() instanceof SyncStartEvent)
        {
            final BeforeCronJobStartEvent syncEvent = ((SyncStartEvent)cockpitEvent.getData()).getSyncEvent();
            if(StringUtils.isNotBlank(syncEvent.getCronJob()))
            {
                return Lists.newArrayList(syncEvent.getCronJob());
            }
        }
        return Collections.emptyList();
    }


    @Override
    public String getEventScope()
    {
        return StringUtils.EMPTY;
    }
}
