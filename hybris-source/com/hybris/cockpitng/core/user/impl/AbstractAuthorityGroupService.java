/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.user.impl;

import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.util.Resettable;
import java.util.List;

/**
 * Service responsible for operations related to authorities
 */
public abstract class AbstractAuthorityGroupService implements AuthorityGroupService
{
    private List<Resettable> groupChangeListeners;


    protected void resetGroupChangeListeners()
    {
        // DE1300
        if(groupChangeListeners != null)
        {
            for(final Resettable listener : groupChangeListeners)
            {
                listener.reset();
            }
        }
    }


    public void setGroupChangeListeners(final List<Resettable> groupChangeListeners)
    {
        this.groupChangeListeners = groupChangeListeners;
    }
}
