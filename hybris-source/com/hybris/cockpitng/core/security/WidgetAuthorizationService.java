/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.security;

import com.hybris.cockpitng.core.Widget;

/**
 * Service for getting info if a widget should be displayed or not.
 */
public interface WidgetAuthorizationService
{
    /**
     * Checks if a widget should be displayed or not, usually based on the widgets access restrictions.
     *
     * @param widget object to check
     * @return whether a widget should be displayed or not, usually based on the widgets access restrictions
     */
    boolean isAccessAllowed(Widget widget);
}
