/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import com.hybris.cockpitng.core.context.CockpitContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.HtmlBasedComponent;

class FallbackDragAndDropStrategy implements DragAndDropStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(FallbackDragAndDropStrategy.class);


    @Override
    public void makeDraggable(final HtmlBasedComponent component, final Object businessObject, final CockpitContext context)
    {
        // NOP
        if(LOG.isDebugEnabled())
        {
            LOG.debug("fallbackStrategy#makeDraggable");
        }
    }


    @Override
    public void makeDraggable(final HtmlBasedComponent component, final Object businessObject, final CockpitContext context,
                    final SelectionSupplier selectionSupplier)
    {
        // NOP
        if(LOG.isDebugEnabled())
        {
            LOG.debug("fallbackStrategy#makeDraggable");
        }
    }


    @Override
    public void makeDroppable(final HtmlBasedComponent component, final Object businessObject, final CockpitContext context)
    {
        // NOP
        if(LOG.isDebugEnabled())
        {
            LOG.debug("fallbackStrategy#makeDroppable");
        }
    }
}
