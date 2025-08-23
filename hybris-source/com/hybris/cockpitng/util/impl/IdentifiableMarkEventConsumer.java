/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl;

import java.util.function.Consumer;
import org.zkoss.zk.ui.event.Event;

/**
 * Object with unique id that is able to consume marked component event.
 * <P>
 * Marked components events listeners may be identifiable to assure that a particular listener is attached only once.
 * </P>
 */
public class IdentifiableMarkEventConsumer extends IdentifiableMarkEventListener
{
    private final Consumer<Event> consumer;


    public IdentifiableMarkEventConsumer(final Object id, final Consumer<Event> consumer)
    {
        super(id);
        this.consumer = consumer;
    }


    @Override
    public void onEvent(final Event event, final Object markData)
    {
        consumer.accept(event);
    }
}
