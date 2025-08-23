/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.core.user.impl;

import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.util.js.JsWidgetSessionDTO;
import com.hybris.cockpitng.util.js.JsWidgetSessionInfoDecorator;
import org.springframework.beans.factory.annotation.Required;

public class AuthorityGroupJsWidgetSessionDecorator implements JsWidgetSessionInfoDecorator
{
    private AuthorityGroupService authorityGroupService;
    private CockpitUserService cockpitUserService;


    @Required
    public void setAuthorityGroupService(final AuthorityGroupService authorityGroupService)
    {
        this.authorityGroupService = authorityGroupService;
    }


    @Required
    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    @Override
    public JsWidgetSessionDTO decorate(final JsWidgetSessionDTO dto)
    {
        final AuthorityGroup activeAuthorityGroupForUser = authorityGroupService
                        .getActiveAuthorityGroupForUser(cockpitUserService.getCurrentUser());
        dto.setActiveAuthorityGroup(activeAuthorityGroupForUser);
        return dto;
    }
}
