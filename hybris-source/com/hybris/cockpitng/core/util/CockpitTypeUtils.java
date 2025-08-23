/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util;

import java.util.List;

/**
 * Provides methods dealing with types in the cockpit, e.g. socket type compatibility
 */
public interface CockpitTypeUtils
{
    /**
     * Checks type compatibility. Customize it to support additional types other than java classes.
     *
     * @param superType
     * @param subType
     * @return true, if superType is assignable from subType, i.e. if superType really is a supertype of subtype.
     */
    boolean isAssignableFrom(String superType, String subType);


    default String findClosestSuperType(final List<Object> entities)
    {
        return Object.class.getCanonicalName();
    }
}
