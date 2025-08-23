/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.mouse;

import java.util.EnumSet;
import java.util.Set;
import org.apache.commons.lang3.BitField;
import org.zkoss.zk.ui.event.MouseEvent;

/**
 * Enum providing support for keys pressed when mouse event occurs
 */
public enum MouseKeys
{
    /** Indicates whether the Alt key is pressed.
     */
    ALT_KEY,
    /** Indicates whether the Ctrl key is pressed.
     */
    CTRL_KEY,
    /** Indicates whether the Shift key is pressed.
     */
    SHIFT_KEY,
    /** Indicates whether the Meta key is pressed.
     */
    META_KEY,
    /** Indicates whether the left button is clicked.
     */
    LEFT_CLICK,
    /** Indicates whether the right button is clicked.
     */
    RIGHT_CLICK,
    /** Indicates whether the middle button is clicked.
     */
    MIDDLE_CLICK;


    /**
     * Extracts keys pressed from zk {@link MouseEvent}
     * @param event zk {@link MouseEvent} to extract keys
     * @return set of keys pressed
     */
    public static Set<MouseKeys> fromMouseEvent(final MouseEvent event)
    {
        final int mask = event.getKeys();
        return fromMouseEventMask(mask);
    }


    /**
     * Extracts keys pressed from zk {@link MouseEvent}
     * @param mask mask of keys defined in zk {@link MouseEvent}
     * @return set of keys pressed
     */
    public static Set<MouseKeys> fromMouseEventMask(final int mask)
    {
        BitField bitField = new BitField(mask);
        final EnumSet<MouseKeys> keys = EnumSet.noneOf(MouseKeys.class);
        addIfSet(keys, bitField, MouseEvent.ALT_KEY, MouseKeys.ALT_KEY);
        addIfSet(keys, bitField, MouseEvent.CTRL_KEY, MouseKeys.CTRL_KEY);
        addIfSet(keys, bitField, MouseEvent.SHIFT_KEY, MouseKeys.SHIFT_KEY);
        addIfSet(keys, bitField, MouseEvent.META_KEY, MouseKeys.META_KEY);
        addIfSet(keys, bitField, MouseEvent.LEFT_CLICK, MouseKeys.LEFT_CLICK);
        addIfSet(keys, bitField, MouseEvent.RIGHT_CLICK, MouseKeys.RIGHT_CLICK);
        addIfSet(keys, bitField, MouseEvent.MIDDLE_CLICK, MouseKeys.MIDDLE_CLICK);
        return keys;
    }


    private static void addIfSet(final Set<MouseKeys> keys, final BitField bitField, final int zkKey, final MouseKeys key)
    {
        if(bitField.isSet(zkKey))
        {
            keys.add(key);
        }
    }
}
