/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions;

import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.spring.SpringUtil;

public class MessageAction implements CockpitAction<Object, Object>
{
    private static final String SETTING_MESSAGE_EXPRESSION = "message";
    private static final String SETTING_MESSAGE_SOCKET = "socket";
    private static final String SETTING_ENABLED_EXPRESSION = "enabled";
    private ExpressionResolverFactory expressionResolverFactory;


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        final ExpressionResolver resolver = getExpressionResolverFactory().createResolver();
        final Object message = resolver.getValue(ctx.getData(), (String)ctx.getParameter(SETTING_MESSAGE_EXPRESSION));
        final ActionResult<Object> result = new ActionResult<>(ActionResult.SUCCESS, message);
        final String socket = (String)ctx.getParameter(SETTING_MESSAGE_SOCKET);
        if(StringUtils.isNotBlank(socket))
        {
            result.addOutputSocketToSend(socket, message);
        }
        return result;
    }


    protected ExpressionResolverFactory getExpressionResolverFactory()
    {
        if(expressionResolverFactory == null)
        {
            expressionResolverFactory = (ExpressionResolverFactory)SpringUtil.getBean("expressionResolverFactory",
                            ExpressionResolverFactory.class);
        }
        return expressionResolverFactory;
    }


    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        if(ctx.getParameterKeys().contains(SETTING_ENABLED_EXPRESSION))
        {
            return BooleanUtils.isTrue(getExpressionResolverFactory().createResolver().getValue(ctx.getData(),
                            (String)ctx.getParameter(SETTING_ENABLED_EXPRESSION)));
        }
        return true;
    }


    @Override
    public boolean needsConfirmation(final ActionContext<Object> ctx)
    {
        return false;
    }


    @Override
    public String getConfirmationMessage(final ActionContext<Object> ctx)
    {
        return "OK";
    }
}
