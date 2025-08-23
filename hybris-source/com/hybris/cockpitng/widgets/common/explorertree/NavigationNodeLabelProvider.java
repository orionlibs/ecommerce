/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree;

import com.hybris.backoffice.navigation.NavigationNode;

/**
 * Provides labels for {@link NavigationNode}s
 */
public interface NavigationNodeLabelProvider
{
    /**
     * Return label for given {@link NavigationNode}
     *
     * @param navNode
     *           node to provide label for
     * @return label
     */
    String getLabel(NavigationNode navNode);
}
