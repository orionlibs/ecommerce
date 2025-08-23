/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.event;

import com.hybris.cockpitng.components.visjs.network.data.EdgeUpdate;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 * Represents an event that indicates a clicking on 'Edit edge' button. The event is thrown after establishing new
 * connection between two nodes
 */
public class EditEdgeEvent extends Event
{
    public static final String NAME = "onEditEdge";


    public EditEdgeEvent(final Component target, final EdgeUpdate edgeUpdate)
    {
        super(NAME, target, edgeUpdate);
    }


    /**
     * @return {@link EdgeUpdate} edgeUpdate consists of two edges. The first one represents previously connection, the
     *         second one represents newly created connection
     */
    public EdgeUpdate getEdge()
    {
        return (EdgeUpdate)getData();
    }
}
