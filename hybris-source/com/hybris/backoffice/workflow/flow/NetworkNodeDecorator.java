/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.hybris.cockpitng.components.visjs.network.data.Node;
import java.util.Map;

/**
 * Decorates the node
 */
@FunctionalInterface
public interface NetworkNodeDecorator
{
    /**
     * Decorates the node
     *
     * @param node
     *           to decorate
     * @param ctx
     *           with additional information
     * @return decorated node
     */
    Node decorate(final Node node, final Map<String, Object> ctx);


    /**
     * Decorates the node
     *
     * @param node
     *           to decorate
     * @return decorated node
     */
    default Node decorate(final Node node)
    {
        return decorate(node, Map.of());
    }
}
