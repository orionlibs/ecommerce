/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow;

import com.hybris.cockpitng.config.jaxb.wizard.PrepareType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 * An interface that defines contract for custom preparation logic for the configurable flow widget. It is used together
 * with {@link com.hybris.cockpitng.config.jaxb.wizard.PrepareType} in the prepare section for the aforementioned
 * widget.
 * <p>
 * Note: handler="spring-bean-id" refers to the bean defined in the spring application context and it is of type
 * {@link FlowPrepareHandler}
 */
public interface FlowPrepareHandler
{
    void prepareFlow(final PrepareType prepare, final WidgetInstanceManager widgetInstanceManager);
}
