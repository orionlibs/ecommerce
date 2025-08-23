/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdate;

/**
 * Renders given object as the {@link com.hybris.cockpitng.components.visjs.network.NetworkChart} component
 */
public interface NetworkEntityRenderer
{
    /**
     * Suggest whether the given object can be handled by the renderer
     *
     * @param workflowEntity
     *           object to be checked if it can be handled
     * @return true if renderer supports the object, false otherwise
     */
    boolean canHandle(WorkflowEntity workflowEntity);


    /**
     * Returns the object's representation in {@link NetworkUpdate} as entity
     *
     * @param workflowEntity
     *           object to be rendered
     * @return objects' representation in {@link NetworkUpdate}
     */
    Node render(WorkflowEntity workflowEntity);


    /**
     * Returns the object's representation in {@link NetworkUpdate} as entity based on already existing node
     *
     * @param workflowEntity
     *           object to be rendered
     * @param node
     *           baseline of new node
     * @return
     */
    Node render(WorkflowEntity workflowEntity, Node node);
}
