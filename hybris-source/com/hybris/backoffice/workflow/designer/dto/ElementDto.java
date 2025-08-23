/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.dto;

import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.core.model.ItemModel;

/**
 * Represents all elements modelled in Workflow Designer
 */
public class ElementDto<MODEL extends ItemModel>
{
    private MODEL model;
    private Operation operation;
    private Node node;


    public MODEL getModel()
    {
        return model;
    }


    public void setModel(final MODEL model)
    {
        this.model = model;
    }


    public Operation getOperation()
    {
        return operation;
    }


    public void setOperation(final Operation operation)
    {
        this.operation = operation;
    }


    public Node getNode()
    {
        return node;
    }


    public void setNode(final Node node)
    {
        this.node = node;
    }
}
