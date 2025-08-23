/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.listener;

import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * ChangeListener for ConfigurableFlow's editors
 */
public class EditorChangeListener implements EventListener<Event>
{
    private final ConfigurableFlowController controller;


    public EditorChangeListener(final ConfigurableFlowController controller)
    {
        this.controller = controller;
    }


    @Override
    public void onEvent(final Event event)
    {
        controller.updateNavigation();
    }
}
