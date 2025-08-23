/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.layout;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;

/**
 * Interface representing a generic layout manager capable of positioning UI elements.
 *
 * @param <T> Type of elements to be positioned with the layout manager
 * @see com.hybris.cockpitng.layout.impl.gridbag.GridBag
 *
 */
public interface LayoutManager<T>
{
    /**
     * Shared key for implementing Drag'n'Drop functionality
     */
    String DND_KEY = "LayoutManager_DnD_key";


    /**
     * This method is responsible for creating the layout.
     *
     * @param parent Parent container for the elements
     * @param placements List of elements to be positioned using the layout
     * @param renderer function used for rendering a single occurrence of a widget
     * @return Collection of components rendered by the manager. The elements may be used to post-process the components,
     *         for example to apply drag'n'drop specific implementation.
     */
    Collection<HtmlBasedComponent> positionElements(Component parent, List<ElementPlacement<T>> placements,
                    BiFunction<Component, ElementPlacement<T>, Component> renderer);
}
