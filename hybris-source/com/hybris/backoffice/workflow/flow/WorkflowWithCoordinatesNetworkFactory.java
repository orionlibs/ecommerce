/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.WorkflowItem;
import com.hybris.backoffice.workflow.designer.WorkflowNetworkEntitiesFactory;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowPojoMapper;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of {@link WorkflowFlowNetworkFactory} which creates a {@link Network} which nodes with predefined X
 * and Y coordinates
 */
public class WorkflowWithCoordinatesNetworkFactory implements WorkflowFlowNetworkFactory
{
    private static final int ORDER_OF_WORKFLOW_WITH_COORDINATES_NETWORK_FACTORY = 10000;
    private int order = ORDER_OF_WORKFLOW_WITH_COORDINATES_NETWORK_FACTORY;
    private WorkflowNetworkEntitiesFactory workflowNetworkEntitiesFactory;
    private WorkflowVisualisationChecker workflowVisualisationChecker;
    private NetworkNodeDecorator networkNodeDecorator;


    @Override
    public boolean canHandle(final Collection<WorkflowItem> items, final NetworkChartContext context)
    {
        return getWorkflowVisualisationChecker().isVisualisationSet(context);
    }


    @Override
    public Network create(final Collection<WorkflowItem> items, final NetworkChartContext context)
    {
        final Optional optional = context.getInitData();
        if(optional.isPresent() && isWorkflowModel(optional.get()))
        {
            return WorkflowPojoMapper.mapItemToWorkflow((ItemModel)optional.get())
                            .map(getWorkflowNetworkEntitiesFactory()::generateNetwork).map(this::decorate).orElse(Network.EMPTY);
        }
        return Network.EMPTY;
    }


    private boolean isWorkflowModel(final Object object)
    {
        return object instanceof WorkflowTemplateModel || object instanceof WorkflowModel;
    }


    protected Network decorate(final Network network)
    {
        return new Network(
                        network.getNodes().stream().map(node -> getNetworkNodeDecorator().decorate(node)).collect(Collectors.toList()),
                        network.getEdges());
    }


    public WorkflowNetworkEntitiesFactory getWorkflowNetworkEntitiesFactory()
    {
        return workflowNetworkEntitiesFactory;
    }


    @Override
    public int getOrder()
    {
        return order;
    }


    // optional
    public void setOrder(final int order)
    {
        this.order = order;
    }


    @Required
    public void setWorkflowNetworkEntitiesFactory(final WorkflowNetworkEntitiesFactory workflowNetworkEntitiesFactory)
    {
        this.workflowNetworkEntitiesFactory = workflowNetworkEntitiesFactory;
    }


    public WorkflowVisualisationChecker getWorkflowVisualisationChecker()
    {
        return workflowVisualisationChecker;
    }


    @Required
    public void setWorkflowVisualisationChecker(final WorkflowVisualisationChecker workflowVisualisationChecker)
    {
        this.workflowVisualisationChecker = workflowVisualisationChecker;
    }


    public NetworkNodeDecorator getNetworkNodeDecorator()
    {
        return networkNodeDecorator;
    }


    @Required
    public void setNetworkNodeDecorator(final NetworkNodeDecorator networkNodeDecorator)
    {
        this.networkNodeDecorator = networkNodeDecorator;
    }
}

