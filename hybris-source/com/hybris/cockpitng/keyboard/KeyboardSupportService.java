/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.keyboard;

import org.zkoss.zk.ui.event.KeyEvent;

/**
 * Keyboard support service
 */
public interface KeyboardSupportService
{
    char SHIFT_PREFIX = '$';
    char ALT_PREFIX = '@';
    char CTRL_PREFIX = '^';
    String F1 = "#f1";
    String F2 = "#f2";
    String F3 = "#f3";
    String F4 = "#f4";
    String F5 = "#f5";
    String F6 = "#f6";
    String F7 = "#f7";
    String F8 = "#f8";
    String F9 = "#f9";
    String F10 = "#f10";
    String F11 = "#f11";
    String F12 = "#f12";
    String PAGE_UP = "#pgup";
    String PAGE_DOWN = "#pgdn";
    String END = "#end";
    String HOME = "#home";
    String LEFT = "#left";
    String UP = "#up";
    String RIGHT = "#right";
    String DOWN = "#down";
    String BACKSPACE = "#bak";
    String INSERT = "#ins";
    String DELETE = "#del";


    /**
     * Checks if given ctrlKeys contains given keyEvent
     *
     * @param ctrlKeys ctrl keys
     * @param keyEvent key event
     * @return true if ctrlKeys contains keyEventCode {@link #convertToKeyEventCode(KeyEvent)}
     */
    boolean containsKeyEvent(String ctrlKeys, KeyEvent keyEvent);


    /**
     * Merges given control keys into one string with no duplicates. All ctrl keys are transformed to lowercase
     *
     * @param ctrlKeysArg list of triggerOnKeys e.g. @0@b@#ins and @1@c@#ins
     * @return merged control keys e.g. @0@b@#ins@1@c
     */
    String mergeCtrlKeys(String... ctrlKeysArg);


    /**
     * Checks if all ctrl keys are valid
     *
     * @param ctrlKeys ctrl keys valid.
     * @return true if ctrl keys are valid.
     */
    boolean validateCtrlKeys(String ctrlKeys);


    /**
     * Converts given keyEvent to string representation. It will contain one of prefixes {@link #SHIFT_PREFIX},
     * {@link #ALT_PREFIX} or {@link #CTRL_PREFIX} depending on modifiers in {@link KeyEvent#isShiftKey()},
     * {@link KeyEvent#isAltKey()}, {@link KeyEvent#isCtrlKey()}
     * <p>
     * e.g. {@link KeyEvent#DELETE} which is represented by 46 and SHIFT pressed it should return $#del
     *
     * @param keyEvent key event
     * @return string representation with modifier
     */
    String convertToKeyEventCode(KeyEvent keyEvent);


    /**
     * Returns a tool tip text for given ctrl keys
     *
     * @param ctrlKeys control keys
     * @return tool tip text
     */
    String getToolTip(String ctrlKeys);
}
