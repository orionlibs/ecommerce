/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowAction;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity;
import com.hybris.cockpitng.components.visjs.network.data.ChosenNode;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.WidthConstraint;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

/**
 * Renderer responsible for displaying action of type {@link WorkflowActionType#NORMAL}
 */
public class ActionNormalRenderer extends AbstractActionRenderer
{
    private static final int DEFAULT_MINIMUM_WIDTH = 100;
    private NodeLabelMapper nodeLabelMapper;
    private int minimumWidth = DEFAULT_MINIMUM_WIDTH;


    @Override
    public boolean canHandle(final WorkflowEntity workflowEntity)
    {
        return workflowEntity instanceof WorkflowAction
                        && WorkflowActionType.NORMAL == ((WorkflowAction)workflowEntity).getActionType();
    }


    @Override
    public Node render(final WorkflowEntity workflowEntity)
    {
        final WorkflowAction workflowAction = (WorkflowAction)workflowEntity;
        final AbstractWorkflowActionModel model = workflowAction.getModel();
        final String group = getActionStatus(model).orElse(WorkflowDesignerGroup.ACTION.getValue());
        return new Node.Builder().withId(String.valueOf(getKeyGenerator().generateFor(model)))
                        .withChosen(new ChosenNode.Builder().withNode(String.valueOf(true)).build())
                        .withX(Optional.ofNullable(model.getVisualisationX()).orElse(0))
                        .withY(Optional.ofNullable(model.getVisualisationY()).orElse(0)).withLabel(nodeLabelMapper.apply(getLabel(model)))
                        .withData(getNodeTypeService().generateNodeData(model)).withGroup(group)
                        .withWidthConstraint(new WidthConstraint.Builder().withMinimum(minimumWidth).build()).withTitle(getTitle(model))
                        .build();
    }


    @Override
    public Node render(final WorkflowEntity workflowEntity, final Node node)
    {
        final WorkflowAction workflowAction = (WorkflowAction)workflowEntity;
        final AbstractWorkflowActionModel model = workflowAction.getModel();
        return new Node.Builder(node).withData(getNodeTypeService().generateNodeData(model))
                        .withLabel(nodeLabelMapper.apply(getLabel(model))).withTitle(getTitle(model)).withShadow(null).build();
    }


    @Required
    public void setNodeLabelMapper(final NodeLabelMapper nodeLabelMapper)
    {
        this.nodeLabelMapper = nodeLabelMapper;
    }


    // optional
    public void setMinimumWidth(final int minimumWidth)
    {
        this.minimumWidth = minimumWidth;
    }
}
