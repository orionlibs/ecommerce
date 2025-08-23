/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.actionbar;

import java.io.Serializable;
import java.util.Collection;

/**
 * Defines a tree structure consisting of {@link ActionDefinition}s.
 */
public interface ActionsTree<DEF extends ActionDefinition> extends Serializable
{
    /**
     * Returns the list of root nodes of the tree.
     *
     * @return list of root nodes of the tree
     */
    Collection<DEF> getRootNodes();
}
