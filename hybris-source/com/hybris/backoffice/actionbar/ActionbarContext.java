/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.actionbar;

import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 * Context interface providing information about actions
 *
 */
public interface ActionbarContext
{
    /**
     *
     * @return listener for action execution
     */
    ActionbarListener getActionListener();


    /**
     *
     * @return {@link WidgetInstanceManager} for widget that defines action bar
     */
    WidgetInstanceManager getWidgetInstanceManager();


    /**
     *
     * @return actions tree for current bar
     */
    ActionsTree getActionTree();
}
