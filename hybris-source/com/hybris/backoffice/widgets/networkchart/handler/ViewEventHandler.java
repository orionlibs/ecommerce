/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.networkchart.handler;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Edges;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;

/**
 * Handler for network chart's events. It can react on click, doubleClick, selection and deselection of nodes and edges.
 */
public interface ViewEventHandler
{
    /**
     * Handles click event on a node.
     *
     * @param node
     *           which has been clicked.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onClick(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles double click event on a node.
     *
     * @param node
     *           which has been double clicked.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onDoubleClick(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles selection event on a node.
     *
     * @param node
     *           which has been selected.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onSelect(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles deselect event on nodes.
     *
     * @param nodes
     *           list of nodes which have been deselected.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onDeselect(final Nodes nodes, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles click event on an edge.
     *
     * @param edge
     *           which has been clicked.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onClick(final Edge edge, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles double click event on an edge.
     *
     * @param edge
     *           which has been double clicked.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onDoubleClick(final Edge edge, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles selection event on an edge.
     *
     * @param edge
     *           which has been selected.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onSelect(final Edge edge, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles deselect event on edges.
     *
     * @param edges
     *           list of edges which have been deselected.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onDeselect(final Edges edges, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles hover event on an edge.
     *
     * @param edge
     *           which has been hovered.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onHover(final Edge edge, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles hover event on a node.
     *
     * @param node
     *           which has been hovered.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onHover(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles blur event on a node.
     *
     * @param node
     *           which has been blurred.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onBlur(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles blur event on an edge.
     *
     * @param edge
     *           which has been blurred.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onBlur(final Edge edge, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * Handles drag end event on a node.
     *
     * @param node
     *           which has been dragged.
     * @param context
     *           with additional information
     * @return {@link NetworkUpdates} changes which should be applied on view.
     */
    default NetworkUpdates onDragEnd(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }
}
