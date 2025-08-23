/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.widgets.networkchart.handler.NetworkPopulator;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import org.springframework.beans.factory.annotation.Required;

/**
 * Notifies {@link #listener} about population and delegates population to {@link #delegate}. The listener will not be
 * notified about empty changes
 */
public class NetworkPopulationDetectingHandler implements NetworkPopulator
{
    private NetworkPopulator delegate;
    private WorkflowDesignerDataManipulationListener listener;


    @Override
    public Network populate(final NetworkChartContext networkChartContext)
    {
        final Network network = getDelegate().populate(networkChartContext);
        if(!Network.EMPTY.equals(network))
        {
            getListener().onNew(networkChartContext.getWim().getModel());
        }
        return network;
    }


    @Override
    public NetworkUpdates update(final Object updatedObject, final NetworkChartContext networkChartContext)
    {
        final NetworkUpdates updates = getDelegate().update(updatedObject, networkChartContext);
        if(!NetworkUpdates.EMPTY.equals(updates))
        {
            getListener().onChange(networkChartContext.getWim().getModel());
        }
        return updates;
    }


    public NetworkPopulator getDelegate()
    {
        return delegate;
    }


    @Required
    public void setDelegate(final NetworkPopulator delegate)
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
