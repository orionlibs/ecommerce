/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetSocket;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Window;

/**
 * Provides functionality for cockpitNG admin mode.
 */
public interface CockpitAdminService
{
    String ADMINMODE_SCLASS = "cng-adminmode";
    String ADMINMODE_SYMBOLIC_SCLASS = "cng-adminmode-symbolic";


    /**
     * Enable or disable the admin mode.
     *
     * @param adminMode True, if adminmode should be enable, false otherwise.
     * @param ref An arbitrary {@link Component} which is attached to the current page.
     */
    void setAdminMode(boolean adminMode, Component ref);


    /**
     * @return true, if adminmode is currently enabled.
     *
     */
    boolean isAdminMode();


    /**
     * @return true, if adminmode is allowed for current session user.
     */
    boolean isAdminModePermitted();


    /**
     * @return true, if symbolic adminmode is currently enabled.
     */
    boolean isSymbolicAdminMode();


    /**
     * @return true, if symbolic adminmode flag is true.
     */
    boolean isSymbolicAdminFlagEnabled();


    /**
     * Sets symbolic adminmode flag.
     * @param enabled indicates whether symbolic admin mode should be enabled
     */
    void setSymbolicAdminFlag(boolean enabled);


    /**
     * Removes a widget from the widget clipboard, if it's present there.
     *
     * @param widget The widget that should be removed.
     */
    void removeWidgetFromClipboard(Widget widget);


    /**
     * 	Checks if given widget is already in the clipboard
     *
     * @param widget The widget that should be checked
     * @return true, if the specified widget is in clipboard.
     */
    boolean isInClipboard(Widget widget);


    /**
     * Adds the specified widget to the clipboard and removes it from the widget tree.
     * @param widget which should be moved to clipboard
     */
    void moveWidgetToClipboard(Widget widget);


    /**
     * @return the current color string used for the widget toolbars in a CSS readable RGB representation.
     */
    String getWidgetToolbarColor();


    /**
     * Opens a wizard for adding a widget to a slot. Shows all widgets in the repository.
     *
     * @param ref An arbitrary {@link Component} which is attached to the current page.
     * @param selectListener An {@link EventListener} which is called when a widget has been selected in the wizard.
     */
    void showAddWidgetWizard(Component ref, EventListener<Event> selectListener);


    /**
     * Render content of widget clipboard.
     *
     * @param clipboardComponent clipborad component
     * @param caption clipborad caption
     */
    void renderWidgetClipboard(Component clipboardComponent, Caption caption);


    /**
     * Opens a wizard where the admin user can group a widget and its children into one single widget.
     *
     * @param widget The root widget of the new widget group.
     * @param onCloseListener An {@link EventListener} which is called when the wizard is closed by the user.
     */
    void showGroupWidgetWizard(Widget widget, EventListener<Event> onCloseListener);


    /**
     * Opens a wizard where a user can connect two widgets.
     *
     * @param source The source widget, i.e. the widget that may fire the event.
     * @param target The target widget, i.e. the widget that should receive the event.
     * @param ref An arbitrary {@link Component} which is attached to the current page.
     */
    void showWidgetConnectionWizard(Widget source, Widget target, Component ref);


    /**
     * Sets a new Color to the toolbar.
     *
     * @param color the new color
     * @param ref An arbitrary {@link Component} which is attached to the current page.
     */
    void setWidgetToolbarColor(String color, Component ref);


    /**
     * Checks if outputSocket from srcWidget can be connected with inputSocket from targetWidget according to data type.
     *
     * @param inputSocket - {@link WidgetSocket} represents input socket
     * @param outputSocket -  {@link WidgetSocket} represents output socket
     * @param srcWidget - (@link {@link Widget} represents source widget
     * @param targetWidget - (@link {@link Widget} represents target widget
     * @return true, if the targetWidget.
     */
    boolean canReceiveFrom(WidgetSocket inputSocket, Widget targetWidget, WidgetSocket outputSocket, Widget srcWidget);


    /**
     * Checks if outputSocket from srcWidget can be connected with inputSocket from targetWidget according to data type.
     *
     * @param inputSocket - {@link WidgetSocket} represents input socket
     * @param outputSocket -  {@link WidgetSocket} represents output socket
     * @return true, if the targetWidget.
     */
    boolean canReceiveFrom(WidgetSocket inputSocket, WidgetSocket outputSocket);


    /**
     * Clears the widget session cache, which results in recalculating the widget tree after the next page refresh.
     */
    void refreshCockpit();


    /**
     * Creates a popup of settings for given widget
     *
     * @param parent a component parent to which a component is appended
     * @param widgetslot a slot of the widget
     * @param widget a widget from which settings to display are taken
     * @param closeCallback a function which is called when settings popup is being close
     * @return created Window
     */
    Window createSettingsWizard(Component parent, Widgetslot widgetslot, Widget widget, EventListener<Event> closeCallback);


    /**
     * Shows a wizard for configuration of input/output sockets.
     *
     * @param widget widget under configuration
     * @param ref parent component
     */
    void showWidgetMultiConnectionWizard(Widget widget, Component ref);


    /**
     * Renders an area of widget definition information
     *
     * @param parent a component parent to which a component is appended
     * @param wiDef a widget definition containing information to display
     * @return created Component
     */
    Component renderWidgetDefinitionInfo(Component parent, WidgetDefinition wiDef);


    /**
     * Sets showConnections flag.
     *
     * @param enabled - value for showConnections flag
     */
    void setShowConnectionsFlagEnabled(boolean enabled);


    /**
     * Checks showConnections flag status
     *
     * @return true, if showConnections flag is true.
     */
    boolean isShowConnectionsFlagEnabled();


    /**
     * Toggles backoffice's admin mode
     *
     * @param ref parent component
     */
    void toggleAdminMode(Component ref);
}
