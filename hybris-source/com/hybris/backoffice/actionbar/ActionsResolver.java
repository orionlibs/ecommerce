/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.actionbar;

import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Collection;

/**
 * Represents an tool object able to resolve list of actions and order them into tree.
 */
public interface ActionsResolver
{
    /**
     * @deprecated since 6.7 - use {@link ActionsResolver#formTree(Collection, WidgetInstanceManager)} Forms provided
     *             actions into the tree
     * @param actions
     *           actions to be shaped
     * @return tree containing all provided actions
     */
    @Deprecated(since = "6.7", forRemoval = true)
    ActionsTree formTree(final Collection<? extends ActionDefinition> actions);


    /**
     * Forms provided actions into the tree
     *
     * @param actions
     *           actions to be shaped
     * @param widgetInstanceManager
     *           widget instance manager
     * @return tree containing all provided actions
     */
    default ActionsTree formTree(final Collection<? extends ActionDefinition> actions,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        return formTree(actions);
    }
}
