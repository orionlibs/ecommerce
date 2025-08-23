/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.WorkflowItem;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.OrderComparator;

/**
 * Default implementation of {@link WorkflowNetworkFactoryStrategy}
 */
public class DefaultWorkflowNetworkFactoryStrategy implements WorkflowNetworkFactoryStrategy
{
    private List<WorkflowFlowNetworkFactory> factories = List.of();


    @Override
    public Optional<WorkflowFlowNetworkFactory> find(final Collection<WorkflowItem> items, final NetworkChartContext context)
    {
        return getFactories().stream().filter(factory -> factory.canHandle(items, context)).findFirst();
    }


    public List<WorkflowFlowNetworkFactory> getFactories()
    {
        return factories;
    }


    @Required
    public void setFactories(final List<WorkflowFlowNetworkFactory> factories)
    {
        if(factories != null)
        {
            OrderComparator.sort(factories);
            this.factories = factories;
        }
    }
}
