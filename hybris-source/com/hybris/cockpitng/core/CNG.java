/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core;

public final class CNG
{
    public static final String PROPERTY_DEVELOPMENT_MODE = "cockpitng.development.mode";
    public static final int NONE = 0;
    public static final int SOCKET_IN = 1;
    public static final int SOCKET_OUT = 1 << 1;
    public static final int SOCKET_VIRTUAL = 1 << 2;


    private CNG()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    public static boolean matchesMask(final int value, final int mask)
    {
        return (value & mask) == mask;
    }
}
