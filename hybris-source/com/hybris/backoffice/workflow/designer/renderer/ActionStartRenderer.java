/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowAction;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import java.util.Optional;

/**
 * Renderer responsible for displaying action of type {@link WorkflowActionType#START}
 */
public class ActionStartRenderer extends AbstractActionRenderer
{
    public static final String VELOCITY_TEMPLATE_LOCATION = "/assets/shapes/startAction.svg.vm";
    public static final String VELOCITY_IE_TEMPLATE_LOCATION = "/assets/shapes/startAction_ie.svg.vm";


    @Override
    public boolean canHandle(final WorkflowEntity workflowEntity)
    {
        return workflowEntity instanceof WorkflowAction
                        && WorkflowActionType.START == ((WorkflowAction)workflowEntity).getActionType();
    }


    @Override
    public Node render(final WorkflowEntity workflowEntity)
    {
        final WorkflowAction workflowAction = (WorkflowAction)workflowEntity;
        final AbstractWorkflowActionModel model = workflowAction.getModel();
        final String group = getActionStatus(model).orElse(WorkflowDesignerGroup.START_ACTION.getValue());
        return new Node.Builder().withId(String.valueOf(getKeyGenerator().generateFor(model)))
                        .withX(Optional.ofNullable(model.getVisualisationX()).orElse(0)).withShape("image")
                        .withY(Optional.ofNullable(model.getVisualisationY()).orElse(0)).withData(getNodeTypeService().generateNodeData(model))
                        .withTitle(getTitle(model)).withGroup(group).withImage(getWorkflowEntityImageCreator().createSvgImage(model,
                                        VELOCITY_TEMPLATE_LOCATION, VELOCITY_IE_TEMPLATE_LOCATION, getCssClass(model)))
                        .build();
    }


    @Override
    public Node render(final WorkflowEntity workflowEntity, final Node node)
    {
        final WorkflowActionTemplateModel model = (WorkflowActionTemplateModel)workflowEntity.getModel();
        return new Node.Builder(node)
                        .withData(getNodeTypeService().generateNodeData(model)).withTitle(getTitle(model)).withImage(getWorkflowEntityImageCreator()
                                        .createSvgImage(model, VELOCITY_TEMPLATE_LOCATION, VELOCITY_IE_TEMPLATE_LOCATION, getCssClass(model)))
                        .withShadow(null).build();
    }
}
