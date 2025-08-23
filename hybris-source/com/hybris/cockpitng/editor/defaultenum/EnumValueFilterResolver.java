/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.defaultenum;

import java.util.List;

/**
 * EnumValueFilterResolver is responsible for providing API for filtering different types of enum values inside
 * EnumMultiReferenceEditors
 */
public interface EnumValueFilterResolver
{
    /**
     * filters values by textQuery and returns a new list containing only matching elements
     *
     * @return filtered list of values
     */
    <T> List<T> filterEnumValues(final List<T> values, final String textQuery);
}
