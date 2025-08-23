/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * An action that shows the data bound to the action.
 */
public class ActionStubExample extends AbstractComponentWidgetAdapterAware implements CockpitAction<String, String>
{
    private static final Logger LOG = LoggerFactory.getLogger(ActionStubExample.class);


    @Override
    public ActionResult<String> perform(final ActionContext<String> ctx)
    {
        sendOutput("testOutput", ctx);
        addSocketInputEventListener("testInput", new EventListener()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                LOG.info("RECEIVED SOCKET INPUT EVENT = " + event);
            }
        });
        return new ActionResult<String>("Dummy Value");
    }


    @Override
    public boolean canPerform(final ActionContext<String> ctx)
    {
        return true;
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
