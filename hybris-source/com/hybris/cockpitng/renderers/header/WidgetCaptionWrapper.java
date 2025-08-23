/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.renderers.header;

/**
 * Wrapper interface to mediate between caption of a widgetContainer and the contained widget.
 *
 * @see WidgetCaptionEventListener
 */
public interface WidgetCaptionWrapper
{
    String ON_WIDGET_COLLAPSE = "onWidgetCollapse";
    String ON_WIDGET_MINIMIZE = "onWidgetMinimize";
    String ON_WIDGET_MAXIMIZE = "onWidgetMaximize";
    String ON_WIDGET_SIZE = "onWidgetSize";
    String ON_WIDGET_FOCUS = "onWidgetFocus";
    String ON_WIDGET_CLOSE = "onWidgetClose";
    String ON_CONTROL_STATE_CHANGE = "onStateChange";
    int CONTROL_CLOSE = 1;
    int CONTROL_COLLAPSE = 2;
    int CONTROL_MINIMIZE = 4;
    int CONTROL_MAXIMIZE = 8;


    /**
     * Returns true if the caption indicates that the container is collapsed.
     */
    boolean isCollapsed();


    /**
     * Expands or collapses the container according to the argument.
     */
    void setCollapsed(boolean collapsed);


    /**
     * Returns true if the caption indicates that the container is minimized.
     */
    boolean isMinimized();


    /**
     * Minimizes/unminimizes the container according to the argument.
     */
    void setMinimized(boolean minimized);


    /**
     * Returns true if the caption indicates that the container is maximized.
     */
    boolean isMaximized();


    /**
     * Maximizes/unmaximizes the container according to the argument.
     */
    void setMaximized(boolean maximized);


    /**
     * Returns true if the caption indicates that the container is focused/selected.
     */
    boolean isFocused();


    /**
     * Sets the focus to (or selects) the current widget within it's widgetchildren container.
     */
    void setFocused(boolean focused);


    /**
     * Closes the container (this usually means that the related widget instance is removed).
     */
    void close();


    /**
     * Returns true if the current state of the container allows collapsing.
     */
    boolean isCollapsible();


    /**
     * Returns true if the current state of the container allows minimizing.
     */
    boolean isMinimizable();


    /**
     * Returns true if the current state of the container allows maximizing.
     */
    boolean isMaximizable();


    /**
     * Returns true if the current state of the container allows closing.
     */
    boolean isClosable();


    /**
     * Call this in your renderer method to hide standard controls from the container, e.g. if you want to render buttons to
     * control minimizing and maximizing of the widget and thus don't need the minimize/maximize buttons of a
     * window/panel/... you could use
     * <p>
     * hideContainerControls(WidgetCaptionWrapper.CONTROL_MINIMIZE | WidgetCaptionWrapper.CONTROL_MAXIMIZE)
     * </p>
     */
    void hideContainerControls(int controls);


    /**
     * @param eventListener
     *           a listener to be notified when an operation happens
     */
    void addListener(final String eventName, final WidgetCaptionEventListener eventListener);
}
