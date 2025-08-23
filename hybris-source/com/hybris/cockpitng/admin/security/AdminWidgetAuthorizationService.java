/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin.security;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.security.WidgetAuthorizationService;
import com.hybris.cockpitng.core.security.impl.DefaultWidgetAuthorizationService;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import org.springframework.beans.factory.annotation.Required;

/**
 * Admin's implementation for {@link WidgetAuthorizationService}.
 * Checks visibility permissions for admin based on his active {@link AuthorityGroup}.
 */
public class AdminWidgetAuthorizationService extends DefaultWidgetAuthorizationService
{
    private CockpitAdminService cockpitAdminService;
    private AuthorityGroupService adminAuthorityGroupService;


    @Override
    protected boolean isAccessAllowedForCurrentUser(final Widget widget)
    {
        final String user = getCockpitUserService().getCurrentUser();
        if(getCockpitUserService().isAdmin(user))
        {
            final AuthorityGroup impersonationAuthorityGroupForUser = getAdminAuthorityGroupService()
                            .getActiveAuthorityGroupForUser(user);
            if(impersonationAuthorityGroupForUser == null)
            {
                // disable restrictions in admin mode if no authority group is set
                if(getCockpitAdminService().isAdminMode())
                {
                    return true;
                }
                else
                {
                    return super.isAccessAllowedForCurrentUser(widget);
                }
            }
            else
            {
                return checkAuthorityMatch(widget.getAccessRestrictions(), impersonationAuthorityGroupForUser.getAuthorities());
            }
        }
        return super.isAccessAllowedForCurrentUser(widget);
    }


    /**
     * @return the adminAuthorityGroupService
     */
    protected AuthorityGroupService getAdminAuthorityGroupService()
    {
        return adminAuthorityGroupService;
    }


    /**
     * @param adminAuthorityGroupService the adminAuthorityGroupService to set
     */
    public void setAdminAuthorityGroupService(final AuthorityGroupService adminAuthorityGroupService)
    {
        this.adminAuthorityGroupService = adminAuthorityGroupService;
    }


    protected CockpitAdminService getCockpitAdminService()
    {
        return cockpitAdminService;
    }


    @Required
    public void setCockpitAdminService(final CockpitAdminService cockpitAdminService)
    {
        this.cockpitAdminService = cockpitAdminService;
    }
}
