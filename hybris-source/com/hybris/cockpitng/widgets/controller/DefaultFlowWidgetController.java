/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default flow container widget controller.
 */
public class DefaultFlowWidgetController extends DefaultWidgetController
{
    protected static final String PAGES_SLOT = "pages";
    protected static final String START = "start";
    protected static final String END_INTERNAL = "endInternal";
    private static final long serialVersionUID = -151292456296500398L;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFlowWidgetController.class);
    private static final String WIZARD_CONTEXT_ATTRIBUTE = "wizard_context";
    private Widgetchildren pageContainer;
    private transient WidgetInstanceFacade widgetInstanceFacade;
    private transient CockpitComponentDefinitionService widgetDefinitionService;


    @SocketEvent(socketId = START)
    public void startWizard(final Object context)
    {
        setContext(context);
        final List<Widget> possibleWidgets = widgetInstanceFacade.getPossibleWidgets(getWidgetslot().getWidgetInstance(),
                        PAGES_SLOT);
        if(!possibleWidgets.isEmpty())
        {
            for(final Widget widget : possibleWidgets)
            {
                final List<WidgetInstance> instances = new ArrayList<WidgetInstance>(widget.getWidgetInstances());
                for(final WidgetInstance instance : instances)
                {
                    widgetInstanceFacade.removeWidgetInstance(instance);
                }
            }
            getWidgetslot().getWidgetInstance().setSelectedChildIndex(0);
        }
        pageContainer.updateView();
        sendOutput("startInternal", context);
    }


    @SocketEvent(socketId = END_INTERNAL)
    public void endInternal(final Object context)
    {
        sendOutput("end", context);
    }


    public void onSocketEvent(final WidgetInstance target, final String eventName, final Object data, final WidgetInstance source,
                    final String sourceSocketId)
    {
        final WidgetInstance widgetInstance = getWidgetslot().getWidgetInstance();
        final List<WidgetInstance> pages = widgetInstanceFacade.getWidgetInstances(widgetInstance, PAGES_SLOT, false);
        if((widgetInstance.equals(source) || pages.contains(source)) && pages.contains(target))
        {
            final WidgetDefinition def = widgetDefinitionService
                            .getComponentDefinitionForCode(target.getWidget().getWidgetDefinitionId(), WidgetDefinition.class);
            if(def != null && def.hasView())
            {
                final int index = pages.indexOf(target);
                widgetInstance.setSelectedChildIndex(index);
                pageContainer.selectChildWidget(index);
            }
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Socket event: " + source + "," + sourceSocketId + "->" + target + "," + eventName + " data: " + data);
        }
    }


    public Object getContext()
    {
        return getModel().getValue(WIZARD_CONTEXT_ATTRIBUTE, Object.class);
    }


    public void setContext(final Object context)
    {
        getModel().setValue(WIZARD_CONTEXT_ATTRIBUTE, context);
    }
}
