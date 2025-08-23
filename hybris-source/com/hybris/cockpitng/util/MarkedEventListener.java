/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import org.zkoss.zk.ui.event.Event;

/**
 * Listener for marked component events.
 * <P>
 * Marked components events listeners may be identifiable to assure that a particular listener is attached only once.
 * </P>
 *
 * @see com.hybris.cockpitng.core.model.Identifiable
 * @see com.hybris.cockpitng.util.impl.IdentifiableMarkEventListener
 */
public interface MarkedEventListener
{
    void onEvent(final Event event, final Object markData);
}
