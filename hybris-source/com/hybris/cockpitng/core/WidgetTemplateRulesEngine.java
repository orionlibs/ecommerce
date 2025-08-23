/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

import com.hybris.cockpitng.core.ui.WidgetInstance;

/**
 * Manages instances of a template widget. Uses template rules to make decision to which instance(s) an incoming socket
 * event is to be sent.
 */
public interface WidgetTemplateRulesEngine
{
    /**
     * Selects target widget instance(s) for the incoming event. It might create new instance as well if rules are set up
     * that way.
     *
     * @param targetWidget
     *           target widget for the incoming event
     * @param targetSocketId
     *           socket ID of the target widget the even is sent to
     * @param sourceInstance
     *           source of the event
     * @param sourceSocketId
     *           socket ID of the source widget the event is sent from
     * @return list of widget instances the event should be sent to. Could be empty list if the event is not applicable
     *         to any of the target widget instances according to rules.
     */
    RuleEngineResult forwardSocketEvent(Widget targetWidget, String targetSocketId, WidgetInstance sourceInstance,
                    String sourceSocketId);


    /**
     * Executes additional logic on an outgoing event according to rules - e.g. closes the source instance.
     *
     * @param sourceInstance source widget of the outgoing event
     * @param sourceSocketId the socket ID the event is sent from
     * @return
     */
    RuleEngineResult handleOutcomingEvent(WidgetInstance sourceInstance, String sourceSocketId);
}
