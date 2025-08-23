/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.create;

import com.hybris.backoffice.workflow.designer.dto.ElementLocation;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

/**
 * Provides initial location of the element placed in Workflow Designer
 */
public interface InitialElementLocationProvider
{
    ElementLocation provideLocation(final WidgetInstanceManager wim);
}
