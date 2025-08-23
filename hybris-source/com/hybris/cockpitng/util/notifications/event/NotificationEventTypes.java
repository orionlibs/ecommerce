/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.notifications.event;

public final class NotificationEventTypes
{
    /**
     * Constant value defining type for notification about object creation
     */
    public static final String EVENT_TYPE_OBJECT_CREATION = "CreateObject";
    /**
     * Constant value defining type for notification about object modification
     */
    public static final String EVENT_TYPE_OBJECT_UPDATE = "UpdateObject";
    /**
     * Constant value defining type for notification about object removal
     */
    public static final String EVENT_TYPE_OBJECT_REMOVAL = "RemoveObject";
    /**
     * Constant value defining type for notification about loading object
     */
    public static final String EVENT_TYPE_OBJECT_LOAD = "LoadObject";
    /**
     * Constant value defining type for notification of general purpose
     */
    public static final String EVENT_TYPE_GENERAL = "General";
    /**
     * Constant value defining type for notification related to permissions
     */
    public static final String EVENT_TYPE_PERMISSIONS = "Permissions";


    private NotificationEventTypes()
    {
        // do nothing
    }
}
