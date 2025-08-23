/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.hybris.cockpitng.components.visjs.network.data.Node;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Allows to adjust nodes rendered by renders used in Workflow Designer feature to nodes used by Show Flow feature
 */
public class WorkflowDesignerToShowFlowNetworkNodeDecorator implements NetworkNodeDecorator
{
    @Override
    public Node decorate(final Node node, final Map<String, Object> ctx)
    {
        final Optional<Integer> level = Optional.ofNullable(ctx.get("level")).filter(Integer.class::isInstance)
                        .map(Integer.class::cast);
        final Optional<String> id = Optional.ofNullable(ctx.get("id")).filter(String.class::isInstance).map(String.class::cast);
        if(level.isPresent() && id.isPresent())
        {
            return Optional.of(Pair.of(level.get(), id.get()))
                            .map(pair -> new Node.Builder(node).withId(pair.getRight()).withLevel(pair.getLeft()).build()).orElse(node);
        }
        return node;
    }
}
