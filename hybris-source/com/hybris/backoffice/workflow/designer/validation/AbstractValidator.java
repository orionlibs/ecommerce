/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import com.hybris.backoffice.workflow.designer.services.ConnectionFinder;
import com.hybris.backoffice.workflow.designer.services.NetworkEntityFinder;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractValidator implements WorkflowDesignerValidator
{
    private NodeTypeService nodeTypeService;
    private NetworkEntityFinder networkEntityFinder;
    private ConnectionFinder connectionFinder;


    public NodeTypeService getNodeTypeService()
    {
        return nodeTypeService;
    }


    @Required
    public void setNodeTypeService(final NodeTypeService nodeTypeService)
    {
        this.nodeTypeService = nodeTypeService;
    }


    public NetworkEntityFinder getNetworkEntityFinder()
    {
        return networkEntityFinder;
    }


    @Required
    public void setNetworkEntityFinder(final NetworkEntityFinder networkEntityFinder)
    {
        this.networkEntityFinder = networkEntityFinder;
    }


    public ConnectionFinder getConnectionFinder()
    {
        return connectionFinder;
    }


    @Required
    public void setConnectionFinder(final ConnectionFinder connectionFinder)
    {
        this.connectionFinder = connectionFinder;
    }
}
