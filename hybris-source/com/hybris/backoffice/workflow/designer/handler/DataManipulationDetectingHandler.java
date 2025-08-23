/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.widgets.networkchart.handler.DataManipulationHandler;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.EdgeUpdate;
import com.hybris.cockpitng.components.visjs.network.data.Edges;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import com.hybris.cockpitng.components.visjs.network.event.ClickOnAddNodeButtonEvent;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.core.model.WidgetModel;
import org.springframework.beans.factory.annotation.Required;

/**
 * Notifies about changes to {@link #listener} and delegates handler methods to {@link #delegate}. The listener will not
 * be notified about empty changes.
 */
public class DataManipulationDetectingHandler implements DataManipulationHandler
{
    private DataManipulationHandler delegate;
    private WorkflowDesignerDataManipulationListener listener;


    @Override
    public NetworkUpdates onAdd(final Node node, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onAdd(node, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onEdit(final Node node, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onEdit(node, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onRemove(final Nodes nodes, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onRemove(nodes, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onAdd(final Edge edge, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onAdd(edge, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onEdit(final EdgeUpdate edgeUpdate, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onEdit(edgeUpdate, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onRemove(final Edges edges, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onRemove(edges, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onAddNodeButtonClick(final ClickOnAddNodeButtonEvent event, final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onAddNodeButtonClick(event, context);
        notifyWhenChangedWithModel(networkUpdates, context);
        return networkUpdates;
    }


    protected void notifyWhenChangedWithModel(final NetworkUpdates networkUpdates, final NetworkChartContext context)
    {
        if(!NetworkUpdates.EMPTY.equals(networkUpdates))
        {
            getListener().onChange(getModel(context));
        }
    }


    @Override
    public NetworkUpdates onSave(final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onSave(context);
        getListener().onNew(getModel(context));
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onRefresh(final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onRefresh(context);
        getListener().onNew(getModel(context));
        return networkUpdates;
    }


    @Override
    public NetworkUpdates onCancel(final NetworkChartContext context)
    {
        final NetworkUpdates networkUpdates = getDelegate().onCancel(context);
        getListener().onNew(getModel(context));
        return networkUpdates;
    }


    protected WidgetModel getModel(final NetworkChartContext context)
    {
        return context.getWim().getModel();
    }


    public DataManipulationHandler getDelegate()
    {
        return delegate;
    }


    @Required
    public void setDelegate(final DataManipulationHandler delegate)
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
