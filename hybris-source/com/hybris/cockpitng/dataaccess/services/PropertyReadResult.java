/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.services;

import java.io.Serializable;

/**
 * Transport object defining a result od property read - contains information about property read status and it's value
 * (if read ended successfully).
 */
public class PropertyReadResult implements Serializable
{
    /**
     * Property read ended successfully
     */
    public static final int READ_STATUS_SUCCESS = 0;
    /**
     * Property read could not be executed because of read restrictions
     */
    public static final int READ_STATUS_RESTRICTED = 1;
    /**
     * An error occurred while processing property expression
     */
    public static final int READ_STATUS_ERROR = 2;
    private final int status;
    private final transient Object value;


    /**
     * Constructor for failed property read
     *
     * @param status
     *           read status describing failure reason
     * @see #READ_STATUS_RESTRICTED
     * @see #READ_STATUS_ERROR
     */
    public PropertyReadResult(final int status)
    {
        this(status, null);
    }


    /**
     * Constructor for successful read
     *
     * @param value
     *           property value
     * @see #READ_STATUS_SUCCESS
     */
    public PropertyReadResult(final Object value)
    {
        this(READ_STATUS_SUCCESS, value);
    }


    private PropertyReadResult(final int status, final Object value)
    {
        this.status = status;
        this.value = value;
    }


    /**
     * Gets property read status
     *
     * @return status of property read
     * @see #READ_STATUS_RESTRICTED
     * @see #READ_STATUS_SUCCESS
     * @see #READ_STATUS_ERROR
     */
    public int getStatus()
    {
        return status;
    }


    public <T extends Serializable> T getValue()
    {
        return (T)value;
    }


    public boolean isSuccessful()
    {
        return getStatus() == READ_STATUS_SUCCESS;
    }


    public boolean isRestricted()
    {
        return getStatus() == READ_STATUS_RESTRICTED;
    }
}
