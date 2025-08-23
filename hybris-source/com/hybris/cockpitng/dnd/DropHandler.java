/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import java.util.List;

/**
 * Handler responsible for dropping operations.
 *
 * @param <DRAGGED>
 *           dragged objects type.
 * @param <TARGET>
 *           target object type.
 */
public interface DropHandler<DRAGGED, TARGET>
{
    /**
     * Handles drop operation.
     *
     * @param dragged
     *           dragged objects.
     * @param target
     *           target object on which dragged objects are dropped.
     * @param context
     *           context of drag and drop operation.
     */
    List<DropOperationData<DRAGGED, TARGET, Object>> handleDrop(List<DRAGGED> dragged, TARGET target,
                    final DragAndDropContext context);


    /**
     * Returns list of droppable types handled. Types are taken literally when followed by "!" or are expanded to subtypes
     * in other cases.
     *
     * @return list of types.
     */
    List<String> findSupportedTypes();
}
