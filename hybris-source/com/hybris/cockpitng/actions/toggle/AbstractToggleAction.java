/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.toggle;

import com.hybris.cockpitng.actions.AbstractStatefulAction;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;

public abstract class AbstractToggleAction extends AbstractStatefulAction<Object, Object>
{
    protected static final String MODEL_VALUE = "toggleValue";
    protected static final String MODEL_ACTIVE = "toggleActive";


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        final ActionResult<Object> actionResult = new ActionResult<>(ActionResult.SUCCESS);
        final String socket = getOutputSocket(ctx);
        final Object value = restoreOutputValue(ctx);
        actionResult.addOutputSocketToSend(socket, value);
        return actionResult;
    }


    /**
     * Reads action's output value from parent's widget model.
     *
     * @param ctx action context
     * @return value read from model
     */
    protected Object restoreOutputValue(final ActionContext<Object> ctx)
    {
        return restoreModelValue(ctx, MODEL_VALUE);
    }


    /**
     * @param ctx action context
     * @return name of output socket for action value
     */
    protected abstract String getOutputSocket(final ActionContext<Object> ctx);
}
