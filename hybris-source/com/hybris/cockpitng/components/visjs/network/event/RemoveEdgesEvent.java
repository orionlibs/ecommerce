/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.event;

import com.hybris.cockpitng.components.visjs.network.data.Edges;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents an event that indicates a clicking on 'Remove edge' button.
 */
public class RemoveEdgesEvent extends Event
{
    public static final String NAME = "onRemoveEdges";


    public RemoveEdgesEvent(final Component target, final Edges edges)
    {
        super(NAME, target, edges);
    }


    /**
     * @return {@link Edges} represents edges which should be removed
     */
    public Edges getEdges()
    {
        return (Edges)getData();
    }
}
