/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dnd;

import com.hybris.cockpitng.core.context.CockpitContext;
import java.util.List;
import org.zkoss.zk.ui.HtmlBasedComponent;

/**
 * Allows to consume drop items by a widget. It can be used as business object for
 * {@link com.hybris.cockpitng.dnd.DragAndDropStrategy#makeDroppable(HtmlBasedComponent, Object, CockpitContext)}
 */
public interface DropConsumer<T>
{
    void itemsDropped(List<T> droppedItems);
}
