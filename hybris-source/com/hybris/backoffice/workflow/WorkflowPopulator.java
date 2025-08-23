/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.widgets.networkchart.handler.NetworkPopulator;
import com.hybris.backoffice.workflow.flow.WorkflowNetworkFactoryStrategy;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

/**
 * Populates the Workflow diagram with data based on {@link NetworkChartContext}
 */
public class WorkflowPopulator implements NetworkPopulator
{
    /**
     * @deprecated since 2005. Use {@link #workflowNetworkFactoryStrategy} instead
     */
    @Deprecated(since = "2005", forRemoval = true)
    private WorkflowNetworkFactory workflowNetworkFactory;
    private WorkflowNetworkFactoryStrategy workflowNetworkFactoryStrategy;
    private WorkflowItemExtractor workflowItemExtractor;


    @Override
    public Network populate(final NetworkChartContext context)
    {
        final Collection<WorkflowItem> items = workflowItemExtractor.extract(context);
        return workflowNetworkFactoryStrategy.find(items, context).map(factory -> factory.create(items, context))
                        .orElse(Network.EMPTY);
    }


    @Override
    public NetworkUpdates update(final Object updatedObject, final NetworkChartContext context)
    {
        return NetworkUpdates.EMPTY;
    }


    /**
     * @deprecated since 2005, not used anymore
     */
    @Deprecated(since = "2005", forRemoval = true)
    public WorkflowNetworkFactory getWorkflowNetworkFactory()
    {
        return workflowNetworkFactory;
    }


    /**
     * @deprecated since 2005, not used anymore
     */
    @Deprecated(since = "2005", forRemoval = true)
    @Required
    public void setWorkflowNetworkFactory(final WorkflowNetworkFactory workflowNetworkFactory)
    {
        this.workflowNetworkFactory = workflowNetworkFactory;
    }


    public WorkflowNetworkFactoryStrategy getWorkflowNetworkFactoryStrategy()
    {
        return workflowNetworkFactoryStrategy;
    }


    @Required
    public void setWorkflowNetworkFactoryStrategy(final WorkflowNetworkFactoryStrategy workflowNetworkFactoryStrategy)
    {
        this.workflowNetworkFactoryStrategy = workflowNetworkFactoryStrategy;
    }


    public WorkflowItemExtractor getWorkflowItemExtractor()
    {
        return workflowItemExtractor;
    }


    @Required
    public void setWorkflowItemExtractor(final WorkflowItemExtractor workflowItemExtractor)
    {
        this.workflowItemExtractor = workflowItemExtractor;
    }
}
