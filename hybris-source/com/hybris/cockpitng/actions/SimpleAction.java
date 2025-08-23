/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

/**
 * An action that shows the data bound to the action.
 */
public class SimpleAction implements CockpitAction<String, String>
{
    @Override
    public ActionResult<String> perform(final ActionContext<String> ctx)
    {
        return new ActionResult<String>("Dummy Value");
    }


    @Override
    public boolean canPerform(final ActionContext<String> ctx)
    {
        final Object data = ctx.getData();
        return (data instanceof String) && (!((String)data).isEmpty());
    }


    @Override
    public boolean needsConfirmation(final ActionContext<String> ctx)
    {
        return true;
    }


    @Override
    public String getConfirmationMessage(final ActionContext<String> ctx)
    {
        return "Ok";
    }
}
