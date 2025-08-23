/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.util;

import com.hybris.cockpitng.core.model.WidgetModel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Support for SPEL expressions
 */
public class ConfigurableFlowExpressions
{
    protected final static Pattern stringLiteralPattern = Pattern.compile("^\\s*'(.*)'\\s*$");


    public Object evalExpression(final WidgetModel model, final String expression)
    {
        Object ret = expression;
        if(null != expression)
        {
            final Matcher matcher = stringLiteralPattern.matcher(expression);
            if(matcher.matches())
            {
                ret = matcher.group(1);
            }
            else
            {
                if(model == null)
                {
                    return null;
                }
                return model.getValue(StringUtils.trim(expression), Object.class);
            }
        }
        return ret;
    }


    public boolean evaluatedExpression2Boolean(final Object result)
    {
        if(result instanceof Boolean)
        {
            return (Boolean)result;
        }
        else
        {
            return result != null;
        }
    }
}
