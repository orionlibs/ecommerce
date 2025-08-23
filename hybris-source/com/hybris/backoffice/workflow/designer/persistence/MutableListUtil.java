/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class MutableListUtil
{
    private MutableListUtil()
    {
        throw new AssertionError("This class should not be instantiated");
    }


    /**
     * Creates mutable list of nullable collection
     *
     * @param collection
     *           nullable collection
     * @param <T>
     *           type of collection
     * @return mutable list based on passed collection
     */
    static <T> List<T> toMutableList(final Collection<T> collection)
    {
        return collection != null ? new ArrayList<>(collection) : new ArrayList<>();
    }
}
