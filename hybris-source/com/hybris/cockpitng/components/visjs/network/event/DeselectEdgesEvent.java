/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.event;

import com.hybris.cockpitng.components.visjs.network.data.Edges;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents an event that indicates a deselecting edges.
 */
public class DeselectEdgesEvent extends Event
{
    public static final String NAME = "onDeselectEdges";


    public DeselectEdgesEvent(final Component target, final Edges edges)
    {
        super(NAME, target, edges);
    }


    /**
     * @return {@link Edges} - edges which have been deselected
     */
    public Edges getEdges()
    {
        return (Edges)getData();
    }
}
