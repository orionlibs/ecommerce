/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowDecision;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Renderer responsible for displaying decision
 */
public class DecisionRenderer implements NetworkEntityRenderer
{
    public static final String VELOCITY_TEMPLATE_LOCATION = "/assets/shapes/decision.svg.vm";
    public static final String VELOCITY_IE_TEMPLATE_LOCATION = "/assets/shapes/decision_ie.svg.vm";
    private KeyGenerator keyGenerator;
    private NodeTypeService nodeTypeService;
    private WorkflowEntityImageCreator workflowEntityImageCreator;


    @Override
    public boolean canHandle(final WorkflowEntity workflowEntity)
    {
        return workflowEntity instanceof WorkflowDecision;
    }


    @Override
    public Node render(final WorkflowEntity workflowEntity)
    {
        final WorkflowDecision workflowDecision = (WorkflowDecision)workflowEntity;
        final AbstractWorkflowDecisionModel model = workflowDecision.getModel();
        return new Node.Builder().withId(String.valueOf(keyGenerator.generateFor(model)))
                        .withX(Optional.ofNullable(model.getVisualisationX()).orElse(0))
                        .withY(Optional.ofNullable(model.getVisualisationY()).orElse(0)).withData(nodeTypeService.generateNodeData(model))
                        .withGroup(WorkflowDesignerGroup.DECISION.getValue()).withImage(workflowEntityImageCreator.createSvgImage(model,
                                        VELOCITY_TEMPLATE_LOCATION, VELOCITY_IE_TEMPLATE_LOCATION, StringUtils.EMPTY))
                        .withTitle(getTitle(model)).build();
    }


    @Override
    public Node render(final WorkflowEntity workflowEntity, final Node node)
    {
        final WorkflowDecision workflowDecision = (WorkflowDecision)workflowEntity;
        final AbstractWorkflowDecisionModel model = workflowDecision.getModel();
        return new Node.Builder(node)
                        .withData(nodeTypeService.generateNodeData(model)).withImage(workflowEntityImageCreator.createSvgImage(model,
                                        VELOCITY_TEMPLATE_LOCATION, VELOCITY_IE_TEMPLATE_LOCATION, StringUtils.EMPTY))
                        .withTitle(getTitle(model)).withShadow(null).build();
    }


    protected String getTitle(final AbstractWorkflowDecisionModel model)
    {
        return StringUtils.isEmpty(model.getName()) ? String.format("[%s]", model.getCode()) : model.getName();
    }


    @Required
    public void setKeyGenerator(final KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }


    @Required
    public void setNodeTypeService(final NodeTypeService nodeTypeService)
    {
        this.nodeTypeService = nodeTypeService;
    }


    @Required
    public void setWorkflowEntityImageCreator(final WorkflowEntityImageCreator workflowEntityImageCreator)
    {
        this.workflowEntityImageCreator = workflowEntityImageCreator;
    }
}
