/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model;

/**
 * Object that can be identified with unique identity.
 */
public interface Identifiable
{
    /**
     * Gets the identity of object.
     * <P>
     * This identity should override {@link Object#equals(Object)} and {@link Object#hashCode()} methods.
     */
    Object getId();
}
