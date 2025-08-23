/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

public interface WidgetDragAndDropStrategyRepository
{
    DragAndDropStrategy getDragAndDropStrategy(final WidgetInstanceManager widgetInstanceManager, final Widget widget);
}
