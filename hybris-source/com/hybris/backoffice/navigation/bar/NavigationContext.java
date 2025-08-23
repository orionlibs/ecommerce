/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.navigation.bar;

import com.hybris.backoffice.actionbar.ActionbarContext;
import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.navigation.NavigationTree;

/**
 * Context interface providing information about perspective chooser
 *
 */
public interface NavigationContext extends ActionbarContext
{
    @Override
    NavigationTree getActionTree();


    /**
     * Checks whether provided node is selected
     *
     * @param node node to be checked
     * @return <code>true</code> if node is currently selected
     * @see #isSelectionParent(NavigationNode)
     */
    boolean isSelected(final NavigationNode node);


    /**
     * Checks whether provided node is a parent of selected one
     *
     * @param node node to be checked
     * @return <code>true</code> if provided node is a parent of selected one
     */
    boolean isSelectionParent(final NavigationNode node);
}
