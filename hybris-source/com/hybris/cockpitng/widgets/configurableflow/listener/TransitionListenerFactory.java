/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.listener;

import com.hybris.cockpitng.config.jaxb.wizard.StepType;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;

/**
 * Factory creating TransitionListener
 */
public class TransitionListenerFactory
{
    private final ConfigurableFlowController controller;


    public TransitionListenerFactory(final ConfigurableFlowController controller)
    {
        this.controller = controller;
    }


    public TransitionListener create(final StepType currentStep, final String action)
    {
        return new TransitionListener(currentStep, action, controller);
    }
}
