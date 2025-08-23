/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.listener;

import com.hybris.cockpitng.config.jaxb.wizard.StepType;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowDefinitions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * Transition listener on wizard.
 */
public class TransitionListener implements EventListener<Event>
{
    private final StepType currentStep;
    private final String action;
    private final ConfigurableFlowController controller;
    private final static boolean SHOULD_VALIDATE = true;


    public TransitionListener(final StepType currentStep, final String action, final ConfigurableFlowController controller)
    {
        this.currentStep = currentStep;
        this.action = action;
        this.controller = controller;
    }


    @Override
    public void onEvent(final Event event)
    {
        switch(action)
        {
            case ConfigurableFlowDefinitions.WIZARD_NEXT:
                controller.doNext(currentStep, SHOULD_VALIDATE);
                break;
            case ConfigurableFlowDefinitions.WIZARD_CANCEL:
                controller.doCancel(currentStep);
                break;
            case ConfigurableFlowDefinitions.WIZARD_DONE:
                controller.doDone(currentStep, SHOULD_VALIDATE);
                break;
            case ConfigurableFlowDefinitions.WIZARD_BACK:
                controller.doBack(currentStep);
                break;
            case ConfigurableFlowDefinitions.WIZARD_CUSTOM:
                controller.doCustom(currentStep, SHOULD_VALIDATE);
                break;
            case ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP_PERSIST:
                controller.doCustomPersist(currentStep, SHOULD_VALIDATE);
                break;
        }
    }
}
