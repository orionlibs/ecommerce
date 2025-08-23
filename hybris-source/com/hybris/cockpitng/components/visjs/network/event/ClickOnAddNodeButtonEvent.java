/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.event;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents an event that indicates a clicking on add node button. In order to enabling that 'customAddNodeButton' has
 * to be set to true.
 */
public class ClickOnAddNodeButtonEvent extends Event
{
    public static final String NAME = "onClickAddNodeButton";


    public ClickOnAddNodeButtonEvent(final Component target, final Object param)
    {
        super(NAME, target, param);
    }
}
