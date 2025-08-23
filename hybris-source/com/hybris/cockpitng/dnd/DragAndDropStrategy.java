/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import com.hybris.cockpitng.core.context.CockpitContext;
import org.zkoss.zk.ui.HtmlBasedComponent;

/**
 * Strategy to handle drag and drop functionality.
 */
public interface DragAndDropStrategy
{
    String ATTRIBUTE_DND_DATA = "dndData";
    String ATTRIBUTE_DND_SELECTION_SUPPLIER = "selectionSupplier";


    /**
     * Makes given {@link HtmlBasedComponent} a draggable component, representing given businessObject.
     *
     * @param component
     *           component to make draggable.
     * @param businessObject
     *           object represented by component.
     * @param context
     *           operation context.
     */
    void makeDraggable(HtmlBasedComponent component, Object businessObject, CockpitContext context);


    /**
     * Makes given {@link HtmlBasedComponent} a draggable component, representing given businessObject.
     *
     * @param component
     *           component to make draggable.
     * @param businessObject
     *           object represented by component.
     * @param context
     *           operation context.
     * @param selectionSupplier
     *           supplier of selected objects.
     */
    void makeDraggable(HtmlBasedComponent component, Object businessObject, CockpitContext context,
                    SelectionSupplier selectionSupplier);


    /**
     * Makes given {@link HtmlBasedComponent} a droppable component, representing given businessObject.
     *
     * @param component
     *           component to make droppable.
     * @param businessObject
     *           object represented by component.
     * @param context
     *           operation context.
     */
    void makeDroppable(HtmlBasedComponent component, Object businessObject, CockpitContext context);
}
