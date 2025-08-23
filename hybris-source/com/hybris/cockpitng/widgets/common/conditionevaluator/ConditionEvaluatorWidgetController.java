/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.conditionevaluator;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.ExpressionException;

public class ConditionEvaluatorWidgetController extends DefaultWidgetController
{
    protected static final String SOCKET_IN_INPUT = "input";
    protected static final String SOCKET_OUT_TRUE = "true";
    protected static final String SOCKET_OUT_FALSE = "false";
    protected static final String SOCKET_OUT_RESULT = "result";
    protected static final String SETTING_EXPRESSION = "expression";
    private static final String SPEL_PARAMETER_WIDGET_MODEL = "widgetModel";
    private static final long serialVersionUID = -4258763632736258164L;
    private static final Logger LOG = LoggerFactory.getLogger(ConditionEvaluatorWidgetController.class);
    private transient ExpressionResolverFactory expressionResolverFactory;


    @SocketEvent
    public void input(final Object data)
    {
        final String expression = getWidgetSettings().getString(SETTING_EXPRESSION);
        if(StringUtils.isBlank(expression))
        {
            sendOutput(SOCKET_OUT_RESULT, Boolean.TRUE);
            sendOutput(SOCKET_OUT_TRUE, data);
        }
        else
        {
            try
            {
                final ExpressionResolver resolver = expressionResolverFactory.createResolver();
                final Boolean result = resolver.getValue(data, expression,
                                Collections.singletonMap(SPEL_PARAMETER_WIDGET_MODEL, getModel()));
                if(Boolean.TRUE.equals(result))
                {
                    sendOutput(SOCKET_OUT_RESULT, Boolean.TRUE);
                    sendOutput(SOCKET_OUT_TRUE, data);
                }
                else
                {
                    sendOutput(SOCKET_OUT_RESULT, Boolean.FALSE);
                    sendOutput(SOCKET_OUT_FALSE, data);
                }
            }
            catch(final ExpressionException e)
            {
                LOG.error("Error evaluating expression '" + expression + "' on data '" + data + "'", e);
            }
        }
    }


    @Required
    public void setExpressionResolverFactory(final ExpressionResolverFactory expressionResolverFactory)
    {
        this.expressionResolverFactory = expressionResolverFactory;
    }
}
