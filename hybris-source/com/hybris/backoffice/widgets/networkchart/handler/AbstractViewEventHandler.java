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
 * Implementation of {@link ViewEventHandler} with empty updates. Extend it in case you want to handle only few events.
 * It allows to reduce boilerplate code.
 */
public abstract class AbstractViewEventHandler implements ViewEventHandler
{
    @Override
    public NetworkUpdates onClick(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onDoubleClick(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onSelect(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onDeselect(final Nodes nodes, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onClick(final Edge edge, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onDoubleClick(final Edge edge, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onSelect(final Edge edge, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onDeselect(final Edges edges, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onHover(final Edge edge, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onHover(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onBlur(final Node node, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    @Override
    public NetworkUpdates onBlur(final Edge edge, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }
}
