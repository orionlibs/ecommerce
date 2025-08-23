/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import org.zkoss.zk.ui.ext.Scope;

public final class ClickTrackingTools
{
    private static final String CLICK_TRACKING_ID = "data-label-key";


    private ClickTrackingTools()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    /**
     * The method sets (changes or adds) click tracking id of the given value.
     *
     * @param component
     *           component on which the value attribute will be set
     * @param value
     *           the value of the clicking tracking id
     */
    public static void modifyClickTrackingId(final Scope component, final String value)
    {
        component.setAttribute(CLICK_TRACKING_ID, value);
    }
}
