/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.table.iterator;

import java.util.Iterator;
import org.zkoss.zk.ui.Component;

/**
 *
 *
 */
public interface TableComponentIterator<E extends Component> extends Iterator<E>
{
    /**
     * Iterator cursor moves forward - if required new element is created.
     *
     * @return
     */
    E request();


    /**
     * Removes all elements that remains ahead current iterator position
     */
    void removeRemaining();
}
