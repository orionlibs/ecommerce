/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.toggle;

import static com.hybris.cockpitng.actions.toggle.AbstractToggleAction.MODEL_ACTIVE;

import com.hybris.cockpitng.actions.AbstractStatefulActionRenderer;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionListener;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.components.Action;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.events.SocketEvent;
import com.hybris.cockpitng.util.UITools;
import org.apache.commons.lang3.BooleanUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;

public abstract class AbstractToggleActionRenderer extends AbstractStatefulActionRenderer<Object, Object>
{
    private static final String ATTRIBUTE_SOCKET_LISTENER = "toggleListener";
    private static final String SCLASS_ACTION = "ya-toggle";
    private static final String SCLASS_INACTIVE = "ya-toggle-inactive";
    private static final String SCLASS_ACTIVE = "ya-toggle-active";


    @Override
    public void render(final Component parent, final CockpitAction<Object, Object> action, final ActionContext<Object> context,
                    final boolean updateMode, final ActionListener<Object> listener)
    {
        super.render(parent, action, context, updateMode, listener);
        initializeState(context);
        updateActiveSClasses((HtmlBasedComponent)parent, isActive(context));
        if(isInputConfigured(context))
        {
            final Action actionComponent = (Action)parent;
            addToggleListener(actionComponent, actionComponent.getWidgetInstanceManager(), context);
        }
    }


    /**
     * Initializes an action state (active/inactive). Action state is automatically stored in widget's model.
     *
     * @param context action context
     */
    protected void initializeState(final ActionContext<Object> context)
    {
        if(getValue(context, AbstractToggleAction.MODEL_VALUE) == null)
        {
            final Object value = getOutputValue(context);
            setValue(context, AbstractToggleAction.MODEL_VALUE, value);
        }
        if(getValue(context, MODEL_ACTIVE) == null)
        {
            setValue(context, MODEL_ACTIVE, Boolean.valueOf(getDefaultActiveState(context)));
        }
    }


    /**
     *
     * @param context action context
     * @return starting state of action
     */
    protected abstract boolean getDefaultActiveState(final ActionContext<Object> context);


    /**
     * Reads action's value
     *
     * @param ctx action context
     * @return a value that will be sent on action perform
     */
    protected abstract Object getOutputValue(final ActionContext<Object> ctx);


    /**
     * Checks if action is currently active
     *
     * @param context action context
     * @return <code>true</code> if action is currently in active state
     */
    protected boolean isActive(final ActionContext<Object> context)
    {
        return BooleanUtils.isTrue(getValue(context, MODEL_ACTIVE));
    }


    protected void updateActiveSClasses(final HtmlBasedComponent component, final boolean active)
    {
        UITools.modifySClass(component, SCLASS_ACTION, true);
        UITools.modifySClass(component, SCLASS_INACTIVE, !active);
        UITools.modifySClass(component, SCLASS_ACTIVE, active);
    }


    protected abstract boolean isActionActivated(final ActionContext<Object> context, final Object inputData);


    protected abstract boolean isInputConfigured(final ActionContext<Object> context);


    protected abstract String getToggleInput(final ActionContext<Object> context);


    protected void addToggleListener(final Action action, final WidgetInstanceManager wim, final ActionContext<Object> context)
    {
        addStateSocketListener(action, context, ATTRIBUTE_SOCKET_LISTENER, getToggleInput(context),
                        () -> initializeToggleListener(action, context));
    }


    protected EventListener<SocketEvent> initializeToggleListener(final Action action, final ActionContext<Object> context)
    {
        return initializeStateSocketListener(action, ATTRIBUTE_SOCKET_LISTENER, createToggleListener(action, context));
    }


    protected EventListener<SocketEvent> createToggleListener(final Action parent, final ActionContext<Object> context)
    {
        return createStateSocketListener(parent, context, MODEL_ACTIVE, data -> Boolean.valueOf(isActionActivated(context, data)));
    }
}
