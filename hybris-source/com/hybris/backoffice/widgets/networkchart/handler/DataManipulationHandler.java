/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.networkchart.handler;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.EdgeUpdate;
import com.hybris.cockpitng.components.visjs.network.data.Edges;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import com.hybris.cockpitng.components.visjs.network.event.ClickOnAddNodeButtonEvent;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;

/**
 * Handler for network chart's structure manipulation. It can react on adding, removing and editing nodes and edges.
 */
public interface DataManipulationHandler
{
    /**
     * Handles event which is triggered after clicking on 'add node' button.
     *
     * @param node
     *           - newly created node. Usually contain only information about its coordinates on a canvas.
     * @param context
     *           - with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onAdd(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles event which is triggered after clicking on 'edit node' button.
     *
     * @param node
     *           - edited node.
     * @param context
     *           - with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onEdit(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles event which is triggered after clicking on 'remove nodes' button.
     *
     * @param nodes
     *           - list of nodes which should be removed.
     * @param context
     *           - with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onRemove(final Nodes nodes, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles event which is triggered after clicking on 'add edge' button.
     *
     * @param edge
     *           - newly created edge.
     * @param context
     *           - with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onAdd(final Edge edge, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles event which is triggered after clicking on 'edit edge' button.
     *
     * @param edgeUpdate
     *           - object contains information about previous connection between two nodes and current connection.
     * @param context
     *           - with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onEdit(final EdgeUpdate edgeUpdate, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles event which is triggered after clicking on 'remove edges' button.
     *
     * @param edges
     *           - list of edges which should be removed.
     * @param context
     *           - with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onRemove(final Edges edges, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles event which is triggered after clicking on 'add node' button. The event is emit only when
     * <b>customAddNodeButton</b> (on Network Chart Widget) setting is set to true.
     *
     * @param event
     *           - represents event which contains element which has been clicked
     * @param context
     *           - with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onAddNodeButtonClick(final ClickOnAddNodeButtonEvent event, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles event which is triggered after clicking on 'save' control button.
     *
     * @param context
     *           with additional information
     */
    default NetworkUpdates onSave(final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles event which is triggered after clicking on 'refresh' control button.
     *
     * @param context
     *           with additional information
     */
    default NetworkUpdates onRefresh(final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles event which is triggered after clicking on 'cancel' control button.
     */
    default NetworkUpdates onCancel(final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }
}
