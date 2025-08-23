/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import org.zkoss.zk.ui.Component;

public final class YTestTools
{
    private static final String YTESTID = "ytestid";
    private static final String ILLEGAL_UUID_CHAR_REPLACEMENT = "_";


    private YTestTools()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    /**
     * The method sets (changes or adds) ytestid of the given value.
     *
     * @param component
     *           component on which the ytestid attribute will be set
     * @param ytestit
     *           the value of the id
     */
    public static void modifyYTestId(final Component component, final String ytestit)
    {
        component.setAttribute(YTESTID, ytestit);
    }


    public static String getYTestId(final Component comp)
    {
        return comp != null ? (String)comp.getAttribute(YTESTID) : null;
    }


    /**
     * Replaces all illegal characters with {@link #ILLEGAL_UUID_CHAR_REPLACEMENT}. UUID can only have alphanumeric
     * characters or underscore.
     *
     * @param ytestId
     * @return
     */
    public static String replaceUUIDIllegalChars(final String ytestId)
    {
        return ytestId != null ? ytestId.replaceAll("[^a-zA-Z0-9_]", ILLEGAL_UUID_CHAR_REPLACEMENT) : null;
    }
}
