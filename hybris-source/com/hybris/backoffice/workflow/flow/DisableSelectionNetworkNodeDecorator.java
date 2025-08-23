/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.hybris.cockpitng.components.visjs.network.data.Image;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link NetworkNodeDecorator} which modifies the nodes definition to disallow their selection
 */
public class DisableSelectionNetworkNodeDecorator implements NetworkNodeDecorator
{
    @Override
    public Node decorate(final Node node, final Map<String, Object> ctx)
    {
        return handleImageNode(node).orElseGet(() -> handleLabelNode(node));
    }


    protected Optional<Node> handleImageNode(final Node node)
    {
        return Optional.ofNullable(node.getImage()).map(Image::getUnselected).map(unselectedImage -> new Node.Builder(node)
                        .withImage(new Image.Builder().withUnselected(unselectedImage).withSelected(null).build()).build());
    }


    protected Node handleLabelNode(final Node node)
    {
        return new Node.Builder(node).withChosen(null).build();
    }
}
