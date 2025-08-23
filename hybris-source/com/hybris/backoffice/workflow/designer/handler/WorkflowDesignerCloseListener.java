/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.engine.impl.ListContainerCloseListener;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 * Listens on Workflow Designer close event and changes made. In case there were changes made in Workflow Designer that
 * are not persisted, the user is asked for confirmation to prevent data loss.
 */
public class WorkflowDesignerCloseListener implements ListContainerCloseListener, WorkflowDesignerDataManipulationListener
{
    public static final String VALUE_CHANGED = "$_value_changed";
    public static final String CLOSE_CONFIRMATION_MESSAGE = "workflow.action.confirmation.message.close";
    public static final String CLOSE_CONFIRMATION_TITLE = "workflow.action.confirmation.title.close";
    private ListContainerCloseListener delegate;


    @Override
    public void onNew(final Object model)
    {
        setValueChangedOnModel(model, false);
    }


    @Override
    public void onChange(final Object model)
    {
        setValueChangedOnModel(model, true);
    }


    private void setValueChangedOnModel(final Object model, final boolean value)
    {
        if(model instanceof Map)
        {
            ((Map<String, Boolean>)model).put(VALUE_CHANGED, value);
        }
    }


    @Override
    public void onClose(final Event event, final WidgetInstance widgetInstance)
    {
        final Object model = widgetInstance.getModel();
        final boolean hasModelChanged = getValueChangedOnModel(model);
        if(hasModelChanged)
        {
            final Window window = (Window)event.getTarget();
            Messagebox.show(getLabel(CLOSE_CONFIRMATION_MESSAGE), getLabel(CLOSE_CONFIRMATION_TITLE),
                            new Messagebox.Button[] {Messagebox.Button.YES, Messagebox.Button.CANCEL}, Messagebox.QUESTION, msgEvent -> {
                                if(Objects.equals(msgEvent.getData(), Messagebox.Button.YES))
                                {
                                    getDelegate().onClose(event, widgetInstance);
                                    window.onClose();
                                }
                            });
            preventWindowFromClosing(event);
        }
        else
        {
            getDelegate().onClose(event, widgetInstance);
        }
    }


    private void preventWindowFromClosing(final Event event)
    {
        event.stopPropagation();
    }


    protected String getLabel(final String key)
    {
        return Labels.getLabel(key);
    }


    private boolean getValueChangedOnModel(final Object model)
    {
        if(model instanceof Map)
        {
            final Object value = ((Map)model).get(VALUE_CHANGED);
            if(value instanceof Boolean)
            {
                return Boolean.TRUE.equals(value);
            }
        }
        return false;
    }


    public ListContainerCloseListener getDelegate()
    {
        return delegate;
    }


    @Required
    public void setDelegate(final ListContainerCloseListener delegate)
    {
        this.delegate = delegate;
    }
}
