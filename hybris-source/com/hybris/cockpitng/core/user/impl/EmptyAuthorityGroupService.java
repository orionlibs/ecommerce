/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.user.impl;

import com.hybris.cockpitng.core.user.AuthorityGroupService;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of AuthorityGroupService which has no authority groups and acts more like a placeholder
 */
public class EmptyAuthorityGroupService implements AuthorityGroupService
{
    @Override
    public AuthorityGroup getActiveAuthorityGroupForUser(final String userId)
    {
        return null;
    }


    @Override
    public List<AuthorityGroup> getAllAuthorityGroupsForUser(final String userId)
    {
        return Collections.emptyList();
    }


    @Override
    public List<AuthorityGroup> getAllAuthorityGroups()
    {
        return Collections.emptyList();
    }


    @Override
    public void setActiveAuthorityGroupForUser(final AuthorityGroup authorityGroup)
    {
        //
    }


    @Override
    public AuthorityGroup getAuthorityGroup(final String code)
    {
        return null;
    }
}
