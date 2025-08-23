/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl;

import java.util.function.Consumer;
import org.zkoss.zk.ui.event.Event;

/**
 * Object with unique id that is able to consume marked component event data.
 * <P>
 * Marked components events listeners may be identifiable to assure that a particular listener is attached only once.
 * </P>
 */
public class IdentifiableMarkDataConsumer extends IdentifiableMarkEventListener
{
    private final Consumer<Object> consumer;


    public IdentifiableMarkDataConsumer(final Object id, final Consumer<Object> consumer)
    {
        super(id);
        this.consumer = consumer;
    }


    @Override
    public void onEvent(final Event event, final Object markData)
    {
        consumer.accept(markData);
    }
}
