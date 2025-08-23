/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultenum;

import java.util.List;

/**
 * Used by {@link DefaultEnumEditor} to retrieve all possible values for an enum or enum-like property type.
 */
public interface EnumValueResolver
{
    /**
     * Returns all available values for the given type and/or initialValue.
     *
     * @param valueType The type of the enumeration.
     * @param value An optional possible value if present. Could improve resolving performance.
     */
    List<Object> getAllValues(String valueType, Object value);
}
