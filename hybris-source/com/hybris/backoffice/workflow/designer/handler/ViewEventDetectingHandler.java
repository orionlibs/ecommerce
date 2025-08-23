/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.widgets.networkchart.handler.ViewEventHandler;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Edges;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import org.springframework.beans.factory.annotation.Required;

/**
 * Notifies about changes to {@link #listener} and delegates view events to {@link #delegate}. The listener will not be
 * notified about empty changes.
 */
public class ViewEventDetectingHandler implements ViewEventHandler
{
    private ViewEventHandler delegate;
    private WorkflowDesignerDataManipulationListener listener;


    @Override
    public NetworkUpdates onClick(final Node node, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onClick(node, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onDoubleClick(final Node node, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onDoubleClick(node, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onSelect(final Node node, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onSelect(node, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onDeselect(final Nodes nodes, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onDeselect(nodes, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onClick(final Edge edge, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onClick(edge, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onDoubleClick(final Edge edge, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onDoubleClick(edge, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onSelect(final Edge edge, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onSelect(edge, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onDeselect(final Edges edges, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onDeselect(edges, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onHover(final Edge edge, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onHover(edge, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onHover(final Node node, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onHover(node, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onBlur(final Node node, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onBlur(node, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onBlur(final Edge edge, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onBlur(edge, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onDragEnd(final Node node, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onDragEnd(node, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    private void notifyWhenChangedWithModel(final NetworkUpdates networkUpdates, final NetworkChartContext context)
    {
        if(!NetworkUpdates.EMPTY.equals(networkUpdates))
        {
            getListener().onChange(context.getWim().getModel());
        }
    }


    public ViewEventHandler getDelegate()
    {
        return delegate;
    }


    @Required
    public void setDelegate(final ViewEventHandler delegate)
    {
        this.delegate = delegate;
    }


    public WorkflowDesignerDataManipulationListener getListener()
    {
        return listener;
    }


    @Required
    public void setListener(final WorkflowDesignerDataManipulationListener listener)
    {
        this.listener = listener;
    }
}
