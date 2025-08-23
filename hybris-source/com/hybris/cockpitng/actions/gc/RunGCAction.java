/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.gc;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;

public class RunGCAction implements CockpitAction<Object, Object>
{
    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        System.gc();
        return new ActionResult<>(ActionResult.SUCCESS, null);
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
        throw new IllegalStateException("This action does not require confirmation");
    }
}
