/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree.data;

/**
 * Allows to define whether given node in
 * {@link com.hybris.cockpitng.widgets.common.explorertree.ExplorerTreeController} widget is action aware or not
 */
public interface ActionAwareNode
{
    /**
     * Defines if node is action aware
     *
     * @return whether is aware or not
     */
    default boolean isActionAware()
    {
        return false;
    }
}
