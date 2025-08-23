/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.multiplexer;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.impl.WidgetSocketUtils;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class MultiplexerController extends DefaultWidgetController
{
    protected static final String SETTING_DEFAULT_SOCKET_OUTPUT = "default_output";
    protected static final String SOCKET_IN_ELEMENT = "element";
    protected static final String SPEL_RULE_SUFFIX = "_expression";
    protected static final String SPEL_OUTPUT_EVAL = "_eval";
    private static final String SPEL_PARAMETER_WIDGET_MODEL = "widgetModel";
    private static final long serialVersionUID = 5633776653059648104L;
    @WireVariable
    private transient WidgetService widgetService;
    private transient ExpressionResolverFactory expressionResolverFactory;


    @SocketEvent(socketId = SOCKET_IN_ELEMENT)
    public void elementInput(final Object element)
    {
        final Widget widget = getWidget();
        final WidgetDefinition widgetDefinition = getWidgetslot().getWidgetDefinition(widget);
        final List<WidgetSocket> outputs = WidgetSocketUtils.getAllOutputs(widget, widgetDefinition);
        boolean nothingWasChosen = true;
        for(final WidgetSocket socket : outputs)
        {
            final String id = socket.getId();
            final String spelRule = getWidgetSettings().getString(id + SPEL_RULE_SUFFIX);
            if(evaluateSpel(element, spelRule))
            {
                final List<WidgetConnection> connections = widgetService.getWidgetConnectionsForOutputWidgetAndSocketID(widget, id);
                if(CollectionUtils.isNotEmpty(connections))
                {
                    nothingWasChosen = false;
                    final String outputEval = getWidgetSettings().getString(id + SPEL_OUTPUT_EVAL);
                    sendOutput(id, evaluateOutput(element, outputEval));
                }
            }
        }
        if(nothingWasChosen)
        {
            final String defaultOutputSocket = this.getWidgetSettings().getString(SETTING_DEFAULT_SOCKET_OUTPUT);
            if(StringUtils.isNotBlank(defaultOutputSocket))
            {
                final String outputEval = getWidgetSettings().getString(defaultOutputSocket + SPEL_OUTPUT_EVAL);
                sendOutput(defaultOutputSocket, evaluateOutput(element, outputEval));
            }
        }
    }


    private boolean evaluateSpel(final Object data, final String expression)
    {
        final boolean isNotBlank = StringUtils.isNotBlank(expression);
        final Object value = expressionResolverFactory.createResolver().getValue(data, expression,
                        Collections.singletonMap(SPEL_PARAMETER_WIDGET_MODEL, getModel()));
        return isNotBlank && Boolean.TRUE.equals(value);
    }


    private Object evaluateOutput(final Object data, final String expression)
    {
        if(StringUtils.isNotEmpty(expression))
        {
            return expressionResolverFactory.createResolver().getValue(data, expression,
                            Collections.singletonMap(SPEL_PARAMETER_WIDGET_MODEL, getModel()));
        }
        return data;
    }


    private Widget getWidget()
    {
        return getWidgetInstance().getWidget();
    }


    private WidgetInstance getWidgetInstance()
    {
        return getWidgetslot().getWidgetInstance();
    }


    public WidgetService getWidgetService()
    {
        return widgetService;
    }


    @Required
    public void setExpressionResolverFactory(final ExpressionResolverFactory expressionResolverFactory)
    {
        this.expressionResolverFactory = expressionResolverFactory;
    }
}
