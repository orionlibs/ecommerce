/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.toggle;

import static com.hybris.cockpitng.actions.toggle.ToggleAction.SETTING_DEFAULT_ACTIVE;
import static com.hybris.cockpitng.actions.toggle.ToggleAction.SETTING_OUTPUT_VALUE;
import static com.hybris.cockpitng.actions.toggle.ToggleAction.SETTING_TOGGLE_CONDITION;
import static com.hybris.cockpitng.actions.toggle.ToggleAction.SETTING_TOGGLE_INPUT;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import java.util.Collection;
import java.util.Objects;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class ToggleActionRenderer extends AbstractToggleActionRenderer
{
    private ExpressionResolverFactory expressionResolverFactory;


    @Override
    protected boolean getDefaultActiveState(final ActionContext<Object> context)
    {
        final String parameter = (String)context.getParameter(SETTING_DEFAULT_ACTIVE);
        final Boolean active;
        if(StringUtils.isNotBlank(parameter))
        {
            active = BooleanUtils.toBooleanObject(parameter);
        }
        else
        {
            active = Boolean.FALSE;
        }
        return active.booleanValue();
    }


    @Override
    protected Object getOutputValue(final ActionContext<Object> ctx)
    {
        return getExpressionResolverFactory().createResolver().getValue(ctx.getData(),
                        (String)ctx.getParameter(SETTING_OUTPUT_VALUE));
    }


    @Override
    protected boolean isInputConfigured(final ActionContext<Object> context)
    {
        return StringUtils.isNotBlank((String)context.getParameter(SETTING_TOGGLE_INPUT));
    }


    @Override
    protected String getToggleInput(final ActionContext<Object> context)
    {
        return (String)context.getParameter(SETTING_TOGGLE_INPUT);
    }


    @Override
    protected boolean isActionActivated(final ActionContext<Object> context, final Object inputData)
    {
        final boolean selected;
        if(context.getParameters().containsKey(SETTING_TOGGLE_CONDITION))
        {
            final ExpressionResolver resolver = getExpressionResolverFactory().createResolver();
            final String expression = (String)context.getParameters().getOrDefault(SETTING_TOGGLE_CONDITION, "#root");
            selected = BooleanUtils.isTrue(resolver.getValue(inputData, expression));
        }
        else
        {
            final Object value = getExpressionResolverFactory().createResolver().getValue(context.getData(),
                            (String)context.getParameter(SETTING_OUTPUT_VALUE));
            if(inputData instanceof Collection)
            {
                selected = ((Collection)inputData).contains(value);
            }
            else
            {
                selected = Objects.equals(inputData, value);
            }
        }
        return selected;
    }


    protected ExpressionResolverFactory getExpressionResolverFactory()
    {
        if(expressionResolverFactory == null)
        {
            expressionResolverFactory = BackofficeSpringUtil.getBean("expressionResolverFactory");
        }
        return expressionResolverFactory;
    }
}
