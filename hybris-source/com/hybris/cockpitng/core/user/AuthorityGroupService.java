/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.user;

import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import java.util.List;

/**
 * Provides functionality for retrieving and activating authority groups.
 *
 */
public interface AuthorityGroupService
{
    /**
     * Returns the active authority group for a specific user.
     *
     * @param userId user ID
     *
     * @return the active authority group for the specified user
     */
    AuthorityGroup getActiveAuthorityGroupForUser(String userId);


    /**
     * Returns a list of all authority groups for the user with the specified ID.
     *
     * @param userId user ID
     *
     * @return list of all the authority groups for the specified user
     */
    List<AuthorityGroup> getAllAuthorityGroupsForUser(String userId);


    /**
     * Returns a list of all available authority groups.
     *
     * @return list of all authority groups in the system
     */
    List<AuthorityGroup> getAllAuthorityGroups();


    /**
     * Marks the provided authority group as the active one.
     *
     * @param authorityGroup the authority group to be marked as being active
     */
    void setActiveAuthorityGroupForUser(AuthorityGroup authorityGroup);


    /**
     * Returns the authority group with the specified code.
     *
     * @param code the code of the authority group to be returned
     * @return the authority group with code <p>code</p>
     *
     */
    AuthorityGroup getAuthorityGroup(String code);
}
