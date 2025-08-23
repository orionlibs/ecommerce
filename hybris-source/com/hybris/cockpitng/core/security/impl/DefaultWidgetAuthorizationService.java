/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.security.impl;

import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.security.WidgetAuthorizationService;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation for {@link WidgetAuthorizationService}.
 * Checks visibility permissions for current user based on his active {@link AuthorityGroup}.
 */
public class DefaultWidgetAuthorizationService implements WidgetAuthorizationService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetAuthorizationService.class);
    private AuthorityGroupService authorityGroupService;
    private CockpitUserService cockpitUserService;


    @Override
    public boolean isAccessAllowed(final Widget widget)
    {
        if(getAuthorityGroupService() != null && getCockpitUserService() != null
                        && CollectionUtils.isNotEmpty(widget.getAccessRestrictions()))
        {
            if(widget.getParent() == null)
            {
                LOG.warn("Access restrictions are not allowed for root widget, ignoring it.");
                return true;
            }
            return isAccessAllowedForCurrentUser(widget);
        }
        return true;
    }


    /**
     * Gets the current session user and checks if he has permisssions to see the widget.
     */
    protected boolean isAccessAllowedForCurrentUser(final Widget widget)
    {
        final String userId = getCockpitUserService().getCurrentUser();
        final AuthorityGroup activeAuthorityGroupForUser = getAuthorityGroupService().getActiveAuthorityGroupForUser(userId);
        if(activeAuthorityGroupForUser == null)
        {
            return CollectionUtils.isEmpty(widget.getAccessRestrictions());
        }
        else
        {
            return checkAuthorityMatch(widget.getAccessRestrictions(), activeAuthorityGroupForUser.getAuthorities());
        }
    }


    /**
     * Does a null-safe {@link CollectionUtils#containsAny(java.util.Collection, java.util.Collection)}.
     */
    protected boolean checkAuthorityMatch(final List<String> list, final List<String> list2)
    {
        if(list == null || list2 == null)
        {
            return false;
        }
        else
        {
            return CollectionUtils.containsAny(list, list2);
        }
    }


    protected AuthorityGroupService getAuthorityGroupService()
    {
        return authorityGroupService;
    }


    public void setAuthorityGroupService(final AuthorityGroupService authorityGroupService)
    {
        this.authorityGroupService = authorityGroupService;
    }


    protected CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }
}
