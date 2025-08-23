/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow;

/**
 * Names of parameters used in ConfigurableFlow context
 */
public enum ConfigurableFlowContextParameterNames
{
    /**
     * Type code
     */
    TYPE_CODE("TYPE_CODE"),
    /**
     * List of properties to exclude from persistence
     */
    NON_PERSISTABLE_PROPERTIES_LIST("nonPersistablePropertiesList"),
    /**
     * Parent object instance
     */
    PARENT_OBJECT("parentObject"),
    /**
     * Parent object type name
     */
    PARENT_OBJECT_TYPE("parentObjectType"),
    /**
     * which Notification Widget should display notification after successful saving object
     */
    SUCCESS_NOTIFICATION_ID("successNotificationId");
    private String name;


    ConfigurableFlowContextParameterNames(final String name)
    {
        this.name = name;
    }


    /**
     * Internal name
     * @return internal name
     */
    public String getName()
    {
        return name;
    }
}
