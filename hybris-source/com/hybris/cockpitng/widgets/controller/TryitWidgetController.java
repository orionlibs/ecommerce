/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * Example widget demonstrating runtime ZUL editing.
 */
public class TryitWidgetController extends DefaultWidgetController
{
    public static final String COMP_ID_TRYIT = "tryIt";
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(TryitWidgetController.class);
    private Window view;
    private Tab demoView;
    private Textbox codeView;
    private Button tryIt;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        if(view != null)
        {
            execute();
        }
    }


    /**
     * Applies new ZUL layout.
     */
    public void execute()
    {
        Components.removeAllChildren(view);
        final String code = codeView.getValue();
        try
        {
            if(tryIt.isVisible())
            {
                Executions.createComponentsDirectly(code, "zul", view, null);
            }
            else
            {
                LOG.warn("Could not apply changes.");
            }
        }
        catch(final RuntimeException e)
        {
            LOG.error("Error caused by: " + code);
            throw e;
        }
    }


    /**
     * Triggers the application of a new layout.
     *
     * @param event received by the component
     */
    @ViewEvent(componentID = COMP_ID_TRYIT, eventName = Events.ON_CLICK)
    public void tryItTrigger(final Event event)
    {
        demoView.setSelected(true);
        execute();
    }


    public Window getView()
    {
        return view;
    }


    public Button getTryIt()
    {
        return tryIt;
    }


    public Textbox getCodeView()
    {
        return codeView;
    }


    public Tab getDemoView()
    {
        return demoView;
    }
}
