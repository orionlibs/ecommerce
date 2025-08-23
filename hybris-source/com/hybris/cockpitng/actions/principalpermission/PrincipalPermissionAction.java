/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.principalpermission;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import javax.annotation.Resource;

public class PrincipalPermissionAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{
    private static final String SOCKET_OUT_CURRENT_PRINCIPAL = "currentPrincipal";
    @Resource
    private transient CockpitUserService cockpitUserService;


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        ActionResult<Object> result = new ActionResult<Object>(ActionResult.ERROR);
        if(ctx.getData() != null)
        {
            sendOutput(SOCKET_OUT_CURRENT_PRINCIPAL, ctx.getData());
            result = new ActionResult<Object>(ActionResult.SUCCESS);
        }
        return result;
    }


    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        final String currentUser = getCockpitUserService().getCurrentUser();
        return getCockpitUserService().isAdmin(currentUser);
    }


    @Override
    public boolean needsConfirmation(final ActionContext<Object> ctx)
    {
        return false;
    }


    @Override
    public String getConfirmationMessage(final ActionContext<Object> ctx)
    {
        return null;
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
