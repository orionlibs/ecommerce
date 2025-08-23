/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationNodeDecorator;
import com.hybris.cockpitng.labels.LabelUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;

/**
 * Default implementation of {@link NavigationNodeLabelProvider}
 */
public class DefaultNavigationNodeLabelProvider implements NavigationNodeLabelProvider
{
    private final ExplorerTreeController explorerTreeController;


    public DefaultNavigationNodeLabelProvider(final ExplorerTreeController explorerTreeController)
    {
        this.explorerTreeController = explorerTreeController;
    }


    @Override
    public String getLabel(final NavigationNode navNode)
    {
        String label = null;
        if(navNode instanceof NavigationNodeDecorator)
        {
            label = ((NavigationNodeDecorator)navNode).getUiLabel(explorerTreeController);
        }
        else if(StringUtils.isNotBlank(navNode.getNameLocKey()))
        {
            label = Labels.getLabel(navNode.getNameLocKey());
        }
        if(StringUtils.isBlank(label))
        {
            if(StringUtils.isNotBlank(navNode.getName()))
            {
                label = navNode.getName();
            }
            else
            {
                label = LabelUtils.getFallbackLabel(navNode.getId());
            }
        }
        return label;
    }
}
