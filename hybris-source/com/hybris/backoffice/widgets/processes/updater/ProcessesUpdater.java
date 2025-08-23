/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.processes.updater;

import com.hybris.cockpitng.core.events.CockpitEvent;
import java.util.List;

/**
 * An interface which allows to update list of processes based on global cockpit event.
 *
 * @deprecated since 6.6 - not used anymore
 */
@Deprecated(since = "6.6", forRemoval = true)
public interface ProcessesUpdater
{
    /**
     * Global event name on which {@link #onEvent(CockpitEvent)} will be called.
     *
     * @return name of global event.
     */
    String getEventName();


    /**
     * Event callback.
     *
     * @param cockpitEvent
     *           global cockpit event.
     * @return list of cronJob codes for which processes should be updated/added to the processes list.
     */
    List<String> onEvent(CockpitEvent cockpitEvent);


    /**
     * Scope of the event {@link CockpitEvent#SESSION},{@link CockpitEvent#APPLICATION} or {@link CockpitEvent#DESKTOP}.
     *
     * @return event scope.
     */
    String getEventScope();
}
