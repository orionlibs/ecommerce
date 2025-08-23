/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.impl;

import com.hybris.cockpitng.core.ui.WidgetInstance;

/**
 * Interface which helps find notification id of topmost Notification Widget. When widget is a template it is displayed
 * in popup. We can display many widget templates and in such case it is important to know where to send notifications
 */
public interface NotificationStack
{
    /**
     * This method is called when new widget template is displayed
     *
     * @param widgetInstance
     */
    void onNewTemplateDisplayed(final WidgetInstance widgetInstance);


    /**
     * This method is called when widget template is closed
     *
     * @param widgetInstance
     */
    void onTemplateClosed(final WidgetInstance widgetInstance);


    /**
     * @return notification id of topmost Notification Widget or null if stack is empty
     */
    String getTopmostId();


    /**
     * @return notification id of second topmost Notification Widget or null if stack's has one or no elements
     */
    String getPreviousId();


    /**
     * Resets notifier stack - removes all notification widget's ids
     */
    void resetNotifierStack();
}
