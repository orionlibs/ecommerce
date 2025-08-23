/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.tree.node.factory.impl;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.impl.SimpleNode;
import com.hybris.cockpitng.tree.node.factory.NodeFactory;
import org.apache.commons.lang3.Validate;

public class SimpleNodeFactory implements NodeFactory<com.hybris.cockpitng.config.explorertree.jaxb.NavigationNode>
{
    @Override
    public NavigationNode createNavigationNode(final com.hybris.cockpitng.config.explorertree.jaxb.NavigationNode nodeConfig)
    {
        Validate.notNull(nodeConfig, "nodeConfig");
        final SimpleNode ret = new SimpleNode(nodeConfig.getId());
        ret.setName(nodeConfig.getId());
        ret.setNameLocKey(nodeConfig.getId());
        ret.setExpandedByDefault(nodeConfig.getExpandedByDefault());
        return ret;
    }
}
