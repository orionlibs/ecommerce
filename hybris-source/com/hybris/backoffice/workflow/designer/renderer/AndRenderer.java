/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.persistence.LinkAttributeAccessor;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowLink;
import com.hybris.cockpitng.components.visjs.network.data.ChosenNode;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import org.springframework.beans.factory.annotation.Required;

/**
 * Renderer responsible for displaying and connection
 */
public class AndRenderer implements NetworkEntityRenderer
{
    public static final String LABEL = "and";
    public static final String AND_CONNECTION_TEMPLATE_PROPERTY = "andConnectionTemplate";
    public static final String AND_CONNECTION_PROPERTY = "andConnection";
    public static final String VISUALISATION_X = "visualisationX";
    public static final String VISUALISATION_Y = "visualisationY";
    private KeyGenerator keyGenerator;


    @Override
    public boolean canHandle(final WorkflowEntity workflowEntity)
    {
        return workflowEntity instanceof WorkflowLink && ((WorkflowLink)workflowEntity).isAndConnection();
    }


    @Override
    public Node render(final WorkflowEntity workflowEntity)
    {
        final WorkflowLink workflowLink = (WorkflowLink)workflowEntity;
        final LinkModel model = workflowLink.getModel();
        final Integer visualizationX = LinkAttributeAccessor.getAttribute(model, VISUALISATION_X, Integer.class).orElse(0);
        final Integer visualizationY = LinkAttributeAccessor.getAttribute(model, VISUALISATION_Y, Integer.class).orElse(0);
        return new Node.Builder().withId(String.valueOf(keyGenerator.generate())).withLabel(LABEL).withX(visualizationX)
                        .withY(visualizationY).withChosen(new ChosenNode.Builder().withNode(String.valueOf(true)).build())
                        .withGroup(WorkflowDesignerGroup.AND.getValue()).build();
    }


    @Override
    public Node render(final WorkflowEntity workflowEntity, final Node node)
    {
        return render(workflowEntity);
    }


    @Required
    public void setKeyGenerator(final KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }
}
