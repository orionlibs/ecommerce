/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.splitlayoutchooser;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;

/**
 * Action responsible for choosing split layout compositions.
 */
public class SplitLayoutChooserAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{
    protected static final String SELECTED_LAYOUT_KEY = "_selectedLayout";


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        final Object data = ctx.getParameter(SELECTED_LAYOUT_KEY);
        return new ActionResult<>(ActionResult.SUCCESS, data);
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
        return "";
    }
}
