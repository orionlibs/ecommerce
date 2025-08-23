/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer;

import com.hybris.backoffice.workflow.designer.pojo.Workflow;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import java.util.Optional;

/**
 * Creates model for visual representation of a workflow in NetworkChart widget
 */
public interface WorkflowNetworkEntitiesFactory
{
    /**
     * Returns the visual representation of given workflow template
     *
     * @param workflow
     *           a template to create the visualisation from
     * @return network object that contains list of nodes and edges
     */
    Network generateNetwork(Workflow workflow);


    /**
     * Returns the visual representation of given item. If no representation is found, {@link Optional#empty()} is
     * returned.
     *
     * @param workflowEntity
     *           an item to create the visualisation from
     * @return node representation of item or {@link Optional#empty()} if not found
     */
    Optional<Node> generateNode(WorkflowEntity workflowEntity);


    /**
     * Returns the visual representation of given item based on existing node. If no representation is found,
     * {@link Optional#empty()} is returned.
     *
     * @param workflowEntity
     *           an item to create the visualisation from
     * @param node
     *           baseline for newly created node
     * @return node representation of item or {@link Optional#empty()} if not found
     */
    Optional<Node> generateNode(WorkflowEntity workflowEntity, Node node);
}
