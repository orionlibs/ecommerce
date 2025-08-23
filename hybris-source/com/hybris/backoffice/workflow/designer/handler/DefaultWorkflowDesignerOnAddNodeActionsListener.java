/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import com.hybris.backoffice.widgets.networkchart.NetworkChartController;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerModelKey;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.zkoss.zk.ui.event.Event;

/**
 * Implementation of listener that sends events of action/decision/andConnection creation via widget sockets
 */
public class DefaultWorkflowDesignerOnAddNodeActionsListener implements WorkflowDesignerOnAddNodeActionsListener
{
    public static final String SOCKET_OUT_CREATE_ACTION = "createAction";
    public static final String SOCKET_OUT_CREATE_DECISION = "createDecision";
    public static final String SOCKET_OUT_CREATE_AND = "createAnd";


    @Override
    public void onAddActionNodeButtonClick(final Event ev, final NetworkChartContext context)
    {
        requireNonNullContext(context);
        context.getWim().sendOutput(SOCKET_OUT_CREATE_ACTION, buildModel(context));
    }


    @Override
    public void onAddDecisionNodeButtonClick(final Event ev, final NetworkChartContext context)
    {
        requireNonNullContext(context);
        context.getWim().sendOutput(SOCKET_OUT_CREATE_DECISION, buildModel(context));
    }


    @Override
    public void onAddAndNodeButtonClick(final Event ev, final NetworkChartContext context)
    {
        requireNonNullContext(context);
        context.getWim().sendOutput(SOCKET_OUT_CREATE_AND, extractWorkflowTemplate(context));
    }


    private static NetworkChartContext requireNonNullContext(final NetworkChartContext context)
    {
        return Objects.requireNonNull(context, "Network Chart Context cannot be null");
    }


    private static Map<String, Object> buildModel(final NetworkChartContext context)
    {
        final WorkflowTemplateModel workflowTemplateModel = extractWorkflowTemplate(context);
        final Set<Node> nodes = extractNodes(context);
        return Map.of(WorkflowDesignerModelKey.KEY_PARENT_OBJECT, workflowTemplateModel, WorkflowDesignerModelKey.KEY_NODES, nodes);
    }


    private static WorkflowTemplateModel extractWorkflowTemplate(final NetworkChartContext context)
    {
        return context.getWim().getModel().getValue(NetworkChartController.MODEL_INIT_DATA, WorkflowTemplateModel.class);
    }


    private static Set<Node> extractNodes(final NetworkChartContext context)
    {
        return context.getWim().getModel().getValue(NetworkChartController.MODEL_NETWORK_NODES, Set.class);
    }
}
