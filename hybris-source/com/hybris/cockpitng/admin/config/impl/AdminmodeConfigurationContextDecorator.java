/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin.config.impl;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.admin.ImpersonationPreviewHelper;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.SessionUserConfigurationContextDecorator;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 */
public class AdminmodeConfigurationContextDecorator extends SessionUserConfigurationContextDecorator
{
    private CockpitAdminService cockpitAdminService;
    private AuthorityGroupService adminModeAuthorityGroupService;
    private ImpersonationPreviewHelper impersonationPreviewHelper;


    @Override
    public <CONFIG> ConfigContext decorateContext(final ConfigContext context, final Class<CONFIG> configurationType,
                    final WidgetInstance widgetInstance)
    {
        if(getAdminModeAuthorityGroupService() != null && getImpersonationPreviewHelper().isImpersonatedPreviewEnabled())
        {
            final String currentUser = getCockpitUserService().getCurrentUser();
            final AuthorityGroup activeAuthorityGroupForUser = getAdminModeAuthorityGroupService()
                            .getActiveAuthorityGroupForUser(currentUser);
            if(activeAuthorityGroupForUser != null)
            {
                final DefaultConfigContext decorated = new DefaultConfigContext();
                for(final String name : context.getAttributeNames())
                {
                    decorated.addAttribute(name, context.getAttribute(name));
                }
                decorated.addAttribute(DefaultConfigContext.CONTEXT_PRINCIPAL, activeAuthorityGroupForUser.getCode());
                return decorated;
            }
        }
        return super.decorateContext(context, configurationType, widgetInstance);
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


    protected AuthorityGroupService getAdminModeAuthorityGroupService()
    {
        return adminModeAuthorityGroupService;
    }


    @Autowired(required = false)
    public void setAdminModeAuthorityGroupService(final AuthorityGroupService adminModeAuthorityGroupService)
    {
        this.adminModeAuthorityGroupService = adminModeAuthorityGroupService;
    }


    protected ImpersonationPreviewHelper getImpersonationPreviewHelper()
    {
        return impersonationPreviewHelper;
    }


    @Required
    public void setImpersonationPreviewHelper(final ImpersonationPreviewHelper impersonationPreviewHelper)
    {
        this.impersonationPreviewHelper = impersonationPreviewHelper;
    }
}
