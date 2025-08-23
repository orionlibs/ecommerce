/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.propextractor;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class PropertyExtractorController extends DefaultWidgetController
{
    public static final String SOCKET_IN_GENERIC_INPUT = "genericInput";
    public static final String SOCKET_OUT_GENERIC_OUTPUT = "genericOutput";
    public static final String SETTING_SPEL_EXPRESSION = "expression";
    public static final String SETTING_IGNORE_NULLS = "ignoreNull";
    private static final String SPEL_PARAMETER_WIDGET_MODEL = "widgetModel";
    private transient ExpressionResolverFactory expressionResolverFactory;


    @SocketEvent(socketId = SOCKET_IN_GENERIC_INPUT)
    public void genericInput(final Object data)
    {
        sendOutput(SOCKET_OUT_GENERIC_OUTPUT, this.evaluateSpel(data, getWidgetSettings().getString(SETTING_SPEL_EXPRESSION)));
    }


    protected Object evaluateSpel(final Object data, final String expression)
    {
        if((data != null || !ignoreNulls()) && StringUtils.isNotBlank(expression))
        {
            final ExpressionResolver resolver = expressionResolverFactory.createResolver();
            return resolver.getValue(data, expression, Collections.singletonMap(SPEL_PARAMETER_WIDGET_MODEL, getModel()));
        }
        else
        {
            return data;
        }
    }


    protected boolean ignoreNulls()
    {
        return (Boolean)getWidgetSettings().getOrDefault(SETTING_IGNORE_NULLS, Boolean.TRUE);
    }


    @Required
    public void setExpressionResolverFactory(final ExpressionResolverFactory expressionResolverFactory)
    {
        this.expressionResolverFactory = expressionResolverFactory;
    }
}
