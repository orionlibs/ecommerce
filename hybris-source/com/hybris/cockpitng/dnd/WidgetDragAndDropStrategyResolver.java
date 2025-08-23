/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

public interface WidgetDragAndDropStrategyResolver
{
    DragAndDropStrategy resolve(final String strategyName);
}
