/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.user;

import java.util.Map;

/**
 * Delivers properties of a user.
 * Properties can be anything, depends on the implementation.
 */
public interface CockpitUserPropertiesService
{
    /**
     * Returns all user properties of the given user.
     *
     * @param userId a non-empty user ID
     * @return all user properties of the given user
     */
    Map<String, String> getUserProperties(String userId);
}
