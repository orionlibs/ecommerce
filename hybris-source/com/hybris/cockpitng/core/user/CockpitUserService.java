/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.user;

/**
 * Service to get and change the current user. Also provides a method for checking whether the current user is an
 * administrator or not.
 */
public interface CockpitUserService
{
    /**
     * Returns the id from the current user.
     *
     * @return value cannot be null.
     */
    String getCurrentUser();


    /**
     * Sets the current user to the user with the specified ID
     *
     * @param userId non-empty user ID
     */
    void setCurrentUser(String userId);


    /**
     * Returns whether the user with the specified ID is an administrator or not.
     *
     * @param userId non-empty user ID
     * @return <p>true</p> if user with the specified ID is an administrator, <p>false</p> otherwise
     */
    boolean isAdmin(String userId);


    /**
     * Returns true if localized editors should be initially expanded for the currently logged user what is defined in the user profile
     * When method is not implemented, <p>false</p> is returned by default from the interface
     * @return <p>true</p> if editor should be expanded or <p>false</p> if not
     */
    default boolean isLocalizedEditorInitiallyExpanded()
    {
        return false;
    }
}
