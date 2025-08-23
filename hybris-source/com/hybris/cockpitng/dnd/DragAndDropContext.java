/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.mouse.MouseKeys;
import java.util.Set;

/**
 * Represents a context passed to drag and drop operations. It allows e.g. pass custom parameters to drop handlers.
 */
public interface DragAndDropContext extends CockpitContext
{
    /**
     * Returns keys hold during drag and drop operation.
     *
     * @return hold keys.
     */
    Set<MouseKeys> getKeys();


    /**
     * Returns a context associated with a dragged object.
     *
     * @return the context object.
     */
    CockpitContext getDraggedContext();


    /**
     * Returns a context associated with a destination object.
     *
     * @return the context object.
     */
    CockpitContext getTargetContext();
}
