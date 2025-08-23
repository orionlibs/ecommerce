/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.processes.updater;

import com.hybris.cockpitng.components.Widgetslot;
import java.util.function.Consumer;

/**
 * @deprecated since 6.6 - not used anymore
 */
@Deprecated(since = "6.6", forRemoval = true)
public interface ProcessesUpdatersRegistry
{
    /**
     * Registers global event listeners on given widgetSlot.
     *
     * @param widgetslot
     *           widget slot on which event listeners will be registered.
     * @param updateCronJob
     *           consumer which updates a cronJob on the list
     */
    void registerGlobalEventListeners(Widgetslot widgetslot, Consumer<String> updateCronJob);
}
