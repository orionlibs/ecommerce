/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.user.impl;

import com.hybris.cockpitng.core.user.CockpitUserService;
import java.io.Serializable;

/**
 * Default implementation of service to get and change the current user.
 */
public class DefaultCockpitUserService implements CockpitUserService, Serializable
{
    private String currentUser;


    @Override
    public String getCurrentUser()
    {
        return currentUser;
    }


    @Override
    public void setCurrentUser(final String userId)
    {
        this.currentUser = userId;
    }


    /**
     * Checks if the given userId belongs to an administrator user. This method should be overridden in child classes
     * otherwise it rises {@link UnsupportedOperationException} when called.
     *
     * @throws UnsupportedOperationException if not overridden.
     */
    @Override
    public boolean isAdmin(final String userId)
    {
        throw new UnsupportedOperationException("Sub-class should override this method.");
    }
}
