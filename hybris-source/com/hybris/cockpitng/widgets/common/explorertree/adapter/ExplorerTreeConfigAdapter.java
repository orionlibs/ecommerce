/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree.adapter;

import com.hybris.cockpitng.config.explorertree.jaxb.ExplorerNode;
import com.hybris.cockpitng.config.explorertree.jaxb.ExplorerTree;
import com.hybris.cockpitng.core.config.CockpitConfigurationAdapter;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.dataaccess.services.PositionedSort;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Cockpit Configuration Adapter which is responsible for arranging tree nodes in proper order for Explorer Tree Widget
 * configuration.
 */
public class ExplorerTreeConfigAdapter implements CockpitConfigurationAdapter<ExplorerTree>
{
    private PositionedSort positionedSort;


    @Required
    public void setPositionedSort(final PositionedSort positionedSort)
    {
        this.positionedSort = positionedSort;
    }


    @Override
    public Class<ExplorerTree> getSupportedType()
    {
        return ExplorerTree.class;
    }


    @Override
    public ExplorerTree adaptAfterLoad(final ConfigContext context, final ExplorerTree explorerTree) throws
                    CockpitConfigurationException
    {
        if(explorerTree != null)
        {
            sortExplorerTreeNodesInDepth(explorerTree.getNavigationNodeOrTypeNodeOrDynamicNode());
        }
        return explorerTree;
    }


    @Override
    public ExplorerTree adaptBeforeStore(final ConfigContext context, final ExplorerTree explorerTree) throws
                    CockpitConfigurationException
    {
        return explorerTree;
    }


    private void sortExplorerTreeNodesInDepth(final List<ExplorerNode> nodes)
    {
        if(CollectionUtils.isEmpty(nodes))
        {
            return;
        }
        positionedSort.sort(nodes);
        for(final ExplorerNode node : nodes)
        {
            sortExplorerTreeNodesInDepth(node.getNavigationNodeOrTypeNodeOrDynamicNode());
        }
    }
}
