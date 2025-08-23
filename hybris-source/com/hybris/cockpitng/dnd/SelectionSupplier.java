/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import java.util.List;

/**
 * Responsible for providing lists of selected objects which are dragged and next dropped.
 *
 * @param <T> selected objects' type.
 */
@FunctionalInterface
public interface SelectionSupplier<T>
{
    /**
     * Returns a list of selected objects which are dragged.
     *
     * @return the list of selected objects which are dragged.
     */
    List<T> findSelection();
}
