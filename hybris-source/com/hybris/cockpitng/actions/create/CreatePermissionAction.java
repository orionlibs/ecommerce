/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.create;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;

public class CreatePermissionAction implements CockpitAction<Object, Object>
{
    protected static final String SELECTED_REFERENCE = "selectedReference";


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        final ActionResult result = new ActionResult<Object>(ActionResult.SUCCESS);
        result.setData(ctx.getParameter(SELECTED_REFERENCE));
        return result;
    }


    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        return true;
    }


    @Override
    public boolean needsConfirmation(final ActionContext<Object> ctx)
    {
        return false;
    }


    @Override
    public String getConfirmationMessage(final ActionContext<Object> ctx)
    {
        throw new UnsupportedOperationException();
    }
}
