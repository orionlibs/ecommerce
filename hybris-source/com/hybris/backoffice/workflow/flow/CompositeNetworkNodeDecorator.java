/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.hybris.cockpitng.components.visjs.network.data.Node;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * Allows to combine multiple {@link NetworkNodeDecorator}s into one bean
 */
public class CompositeNetworkNodeDecorator implements NetworkNodeDecorator
{
    private List<NetworkNodeDecorator> networkNodeDecorators;


    @Override
    public Node decorate(final Node node, final Map<String, Object> ctx)
    {
        Node decoratedNode = node;
        for(final NetworkNodeDecorator decorator : networkNodeDecorators)
        {
            decoratedNode = decorator.decorate(decoratedNode, ctx);
        }
        return decoratedNode;
    }


    @Required
    public void setNetworkNodeDecorators(final List<NetworkNodeDecorator> networkNodeDecorators)
    {
        this.networkNodeDecorators = networkNodeDecorators;
    }
}
