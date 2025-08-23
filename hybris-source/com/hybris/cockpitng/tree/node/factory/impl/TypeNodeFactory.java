/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.node.factory.impl;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.cockpitng.tree.node.TypeNode;
import com.hybris.cockpitng.tree.node.factory.NodeFactory;
import org.apache.commons.lang3.Validate;

public class TypeNodeFactory implements NodeFactory<com.hybris.cockpitng.config.explorertree.jaxb.TypeNode>
{
    @Override
    public NavigationNode createNavigationNode(final com.hybris.cockpitng.config.explorertree.jaxb.TypeNode nodeConfig)
    {
        Validate.notNull(nodeConfig, "nodeConfig");
        final TypeNode ret = new TypeNode(nodeConfig.getId(), nodeConfig.getCode());
        ret.setName(nodeConfig.getCode());
        ret.setNameLocKey(nodeConfig.getId());
        return ret;
    }
}
