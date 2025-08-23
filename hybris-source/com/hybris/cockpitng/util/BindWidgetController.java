/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.WidgetController;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.WidgetInstanceManagerAware;
import java.lang.reflect.Method;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.zkoss.bind.BindComposer;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

/**
 * A MVVM bind composer based widget controller. Used as a widget controller for widgets with a MVVM view model.
 * It is not to be used directly. To access the widget framework functionality from your MVVM view model you need
 * to make it WidgetInstanceManager-aware by implementing the interface {@link WidgetInstanceManagerAware}. The injected
 * WidgetInstanceManager will allow you to send data to output sockets, access the widget settings and the widget model
 * to store view related data in the session scope.
 */
public class BindWidgetController extends BindComposer<Component> implements WidgetController, WidgetInstanceManagerAware
{
    private static final Logger LOG = LoggerFactory.getLogger(BindWidgetController.class);
    private static final long serialVersionUID = -7469725209813368165L;
    private Widgetslot widgetslot;
    private String viewModelID;
    private transient WidgetDefinition definition;
    private transient WidgetInstanceManager widgetInstanceManager;
    @WireVariable
    private transient CockpitWidgetEngine cockpitWidgetEngine;
    @WireVariable
    private transient WidgetUtils widgetUtils;


    @Override
    public void doBeforeComposeChildren(final Component comp) throws Exception
    {
        final Object attribute = comp.getParent().getAttribute("moduleAppCtx");
        if(attribute instanceof ApplicationContext)
        {
            final ApplicationContext appCtx = (ApplicationContext)attribute;
            final VariableResolver varRes = new VariableResolver()
            {
                @Override
                public Object resolveVariable(final String name)
                {
                    if("widgetInstanceManager".equals(name))
                    {
                        return null;
                    }
                    else
                    {
                        try
                        {
                            return appCtx.getBean(name);
                        }
                        catch(final BeansException e)
                        {
                            LOG.error("Could not find bean '" + name + "' in module application context", e);
                            return null;
                        }
                    }
                }
            };
            Selectors.wireVariables(comp, getViewModel(), Collections.singletonList(varRes));
        }
        super.doBeforeComposeChildren(comp);
        WidgetControllers.initSettings(comp, widgetInstanceManager);
    }


    @Override
    public ComponentInfo doBeforeCompose(final Page page, final Component parent, final ComponentInfo compInfo)
                    throws Exception
    {
        final ComponentInfo doBeforeCompose = super.doBeforeCompose(page, parent, compInfo);
        Selectors.wireVariables(page, this, Collections.<VariableResolver>emptyList());
        return doBeforeCompose;
    }


    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        final Object viewModel = getViewModel();
        comp.setAttribute(getViewModelID(), viewModel);
        comp.setAttribute("$VM_ID$", getViewModelID());
        bindSockets(comp, viewModel, definition);
        super.doAfterCompose(comp);
    }


    protected void bindSockets(final Component comp, final Object viewModel, final WidgetDefinition widgetDefinition)
    {
        final Method[] methods = viewModel.getClass().getMethods();
        for(final Method method : methods)
        {
            // check for notify annos
            final NotifyChange annotation = method.getAnnotation(NotifyChange.class);
            final String[] notifyProps = annotation == null ? null : annotation.value();
            final String socketId = WidgetControllers.getSocketEventIdIfAny(method);
            if(socketId != null)
            {
                final Class<?>[] parameterTypes = method.getParameterTypes();
                if(parameterTypes != null && parameterTypes.length == 1)
                {
                    final String eventName = "onSocketInput_" + socketId;
                    comp.addEventListener(eventName, new EventListener<Event>()
                    {
                        @Override
                        public void onEvent(final Event event) throws Exception
                        {
                            WidgetControllers.fireEvent(viewModel, event, method);
                            if(notifyProps != null)
                            {
                                for(final String string : notifyProps)
                                {
                                    notifyChange(viewModel, string);
                                }
                            }
                        }
                    });
                }
                else
                {
                    LOG.error("Error while binding socket to viewmodel of '" + widgetDefinition.getCode()
                                    + "', method signature doesn't match requirements.");
                }
            }
            WidgetControllers.bindGlobalEventListenerIfPresent(method, viewModel, widgetInstanceManager, widgetUtils,
                            new EventListener<Event>()
                            {
                                @Override
                                public void onEvent(final Event event) throws Exception
                                {
                                    if(notifyProps != null)
                                    {
                                        for(final String string : notifyProps)
                                        {
                                            notifyChange(viewModel, string);
                                        }
                                    }
                                }
                            });
        }
    }


    public void setWidgetslot(final Widgetslot widgetslot)
    {
        this.widgetslot = widgetslot;
    }


    public void sendOutput(final String socketId, final Object data)
    {
        try
        {
            cockpitWidgetEngine.sendOutput(widgetslot, socketId, data);
        }
        catch(final Exception e)
        {
            LOG.error("could not send output, error: ", e);
        }
    }


    @Override
    public TypedSettingsMap getWidgetSettings()
    {
        return widgetslot.getWidgetInstance().getWidget().getWidgetSettings();
    }


    public void setViewModelId(final String viewModelIDArg)
    {
        this.viewModelID = viewModelIDArg;
    }


    public String getViewModelID()
    {
        return StringUtils.isBlank(viewModelID) ? "vm" : viewModelID;
    }


    public void setWidgetDefinition(final WidgetDefinition definitionArg)
    {
        this.definition = definitionArg;
    }


    @Override
    public WidgetModel getModel()
    {
        return widgetslot.getViewModel();
    }


    @Override
    public void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }
}
