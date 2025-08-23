/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.core.model.Identifiable;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * An {@link EventListener} that has a unique identity (implements {@link Identifiable}
 */
public interface IdentifiableEventListener<E extends Event> extends EventListener<E>, Identifiable
{
}
