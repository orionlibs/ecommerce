/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.dndconfig;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;

public class DragAndDropConfigAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{
    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        return new ActionResult<>(ActionResult.SUCCESS);
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
}
