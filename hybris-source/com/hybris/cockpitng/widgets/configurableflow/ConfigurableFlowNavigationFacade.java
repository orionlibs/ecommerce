/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow;

/**
 * Class exposes navigation through the ConfigurableFlow
 */
public class ConfigurableFlowNavigationFacade
{
    private final ConfigurableFlowController controller;


    /**
     * Creates an instance of ConfigurableFlowNavigationFacade
     *
     * @param controller of configurable flow
     */
    public ConfigurableFlowNavigationFacade(final ConfigurableFlowController controller)
    {
        this.controller = controller;
    }


    /**
     * Proceeds to next wizard step
     */
    public void next()
    {
        controller.doNext(controller.getCurrentStep());
    }


    /**
     * Steps back
     */
    public void back()
    {
        controller.doBack(controller.getCurrentStep());
    }


    /**
     * Cancels wizard's progress
     */
    public void cancel()
    {
        controller.doCancel(controller.getCurrentStep());
    }


    /**
     * Confirms wizard's progress
     */
    public void done()
    {
        controller.doDone(controller.getCurrentStep(), true);
    }
}
